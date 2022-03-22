package com.group5.apigateway.config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.Claim;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.*;
import java.util.regex.Pattern;

@RefreshScope
@Component
@Slf4j
public class AuthenticationFilter implements GatewayFilter {

    /* The endpoints in here can be accessed without any permission
     */
    private final List<Request> OPEN_ENDPOINTS = new LinkedList<>(){{
        add(new Request(HttpMethod.PUT, Pattern.compile("\\/api\\/ds\\/user\\/[a-zA-Z0-9]+\\/update\\/box\\/[a-zA-Z0-9]+")));
        add(new Request(HttpMethod.POST, Pattern.compile("\\/api\\/cas\\/login")));
        add(new Request(HttpMethod.GET, Pattern.compile("\\/api/cas\\/users\\/token\\/")));
    }};

    /**
     * Maps the urls to the list of roles that are allowed to access it
     * If a url is not present here nor in the PERMISSIONLESS_ENDPOINTS then
     * It is not accessible from outside and only allowed to be called by other services
     */
    private final Map<Request, ImmutableList<String>> RESTRICTED_ENDPOINTS = new HashMap<>(){{
        put(new Request(HttpMethod.GET, Pattern.compile("\\/api\\/cas\\/users\\/[a-zA-Z0-9]+")), ImmutableList.of("DISPATCHER"));
        put(new Request(HttpMethod.POST, Pattern.compile("\\/api\\/cas\\/users")), ImmutableList.of("DISPATCHER"));
        put(new Request(HttpMethod.PUT, Pattern.compile("\\/api\\/cas\\/users")), ImmutableList.of("DISPATCHER"));
        put(new Request(HttpMethod.GET, Pattern.compile("\\/api\\/cas\\/users\\/[a-zA-Z0-9]+\\/role")), ImmutableList.of("DISPATCHER"));
        put(new Request(HttpMethod.DELETE, Pattern.compile("\\/api\\/cas\\/users\\/[a-zA-Z0-9]+")), ImmutableList.of("DISPATCHER"));

        put(new Request(HttpMethod.POST, Pattern.compile("\\/api\\/ds\\/boxes")), ImmutableList.of("DISPATCHER"));
        put(new Request(HttpMethod.PUT, Pattern.compile("\\/api\\/ds\\/boxes\\/[a-zA-Z0-9]+")), ImmutableList.of("DISPATCHER"));
        put(new Request(HttpMethod.DELETE, Pattern.compile("\\/api\\/ds\\/boxes\\/[a-zA-Z0-9]+")), ImmutableList.of("DISPATCHER"));
        put(new Request(HttpMethod.DELETE, Pattern.compile("\\/api\\/ds\\/boxes\\/deliverer\\/[a-zA-Z0-9]+")), ImmutableList.of("DISPATCHER", "DELIVERER"));

        put(new Request(HttpMethod.PUT, Pattern.compile("\\/api\\/ds\\/box\\/[a-zA-Z0-9]+\\/collected\\/[a-zA-Z0-9]+\\/deliverer\\/[a-zA-Z0-9]+")), ImmutableList.of("DISPATCHER"));
        put(new Request(HttpMethod.PUT, Pattern.compile("\\/api\\/ds\\/deliverer\\/[a-zA-Z0-9]+\\/deposited\\/box\\/[a-zA-Z0-9]+")), ImmutableList.of("DISPATCHER"));
        put(new Request(HttpMethod.PUT, Pattern.compile("\\/api\\/ds\\/user\\/[a-zA-Z0-9]+\\/delivered\\/box\\/[a-zA-Z0-9]+")), ImmutableList.of("DISPATCHER"));

        put(new Request(HttpMethod.PUT, Pattern.compile("\\/api\\/ds\\/deliveries")), ImmutableList.of("DISPATCHER"));
        put(new Request(HttpMethod.GET, Pattern.compile("\\/api\\/ds\\/deliveries\\/[a-zA-Z0-9]+")), ImmutableList.of("DISPATCHER", "CUSTOMER"));
        put(new Request(HttpMethod.GET, Pattern.compile("\\/api\\/ds\\/deliveries\\/customer\\/[a-zA-Z0-9]+\\/status\\/delivered")), ImmutableList.of("DISPATCHER", "CUSTOMER"));
        put(new Request(HttpMethod.GET, Pattern.compile("\\/api\\/ds\\/deliveries\\/customer\\/[a-zA-Z0-9]+\\/status\\/active")), ImmutableList.of("DISPATCHER", "CUSTOMER"));

        put(new Request(HttpMethod.POST, Pattern.compile("\\/api\\/ds\\/deliveries")), ImmutableList.of("DISPATCHER"));
        put(new Request(HttpMethod.DELETE, Pattern.compile("\\/api\\/ds\\/deliveries\\/[a-zA-Z0-9]+")), ImmutableList.of("DISPATCHER"));
    }};

    public AuthenticationFilter() {
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        var path = request.getURI().getPath();

        if (isOpenEndpoint(request, path)) {
            return chain.filter(exchange);
        }

        for (Request reqDefinition : RESTRICTED_ENDPOINTS.keySet()) {
            if (reqDefinition.getPattern().matcher(path).matches() && reqDefinition.getHttpMethod().equals(request.getMethod())) {
                if (isAuthMissing(request)) {
                    Mono<Void> response = respondWithUnauthorized(exchange, "Token is empty");
                    if (response != null) return response;
                }

                var authHeader = getAuthHeader(request);
                var tokenString = authHeader.split(" ")[1];
                var token = JWT.decode(tokenString);

                var allowedRoles = RESTRICTED_ENDPOINTS.get(reqDefinition);
                Map<String, Claim> claims = token.getClaims();

                if (claims == null) {
                    Mono<Void> response = respondWithUnauthorized(exchange, "Unauthorized");
                    if (response != null) return response;
                }

                var authorities = claims.get("authorities");
                var roleInToken = authorities.asString().split("_")[1];

                if (!allowedRoles.contains(roleInToken)) {
                    Mono<Void> response = respondWithUnauthorized(exchange, "Unauthorized");
                    if (response != null) return response;
                }
            }
        }

        return chain.filter(exchange);
    }

    private Mono<Void> respondWithUnauthorized(ServerWebExchange exchange, String cause) {
        var response = exchange.getResponse();
        Map<String, Object> responseData = Maps.newHashMap();
        responseData.put("code", 401);
        responseData.put("message", "Illegal request");
        responseData.put("cause", cause);

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            byte[] data = objectMapper.writeValueAsBytes(responseData);

            DataBuffer buffer = response.bufferFactory().wrap(data);
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            response.getHeaders().add("Content-Type", "application/json;charset=UTF-8");
            return response.writeWith(Mono.just(buffer));
        } catch (JsonProcessingException e) {
            log.error("{}", e);
        }
        return null;
    }

    private boolean isOpenEndpoint(ServerHttpRequest request, String path) {
        return OPEN_ENDPOINTS.stream().anyMatch(req -> req.getPattern().matcher(path).matches() && Objects.equals(request.getMethod(), req.getHttpMethod()));
    }

    private String getAuthHeader(ServerHttpRequest request) {
        return request.getHeaders().getOrEmpty("Authorization").get(0);
    }

    private boolean isAuthMissing(ServerHttpRequest request) {
        return !request.getHeaders().containsKey("Authorization");
    }
}
