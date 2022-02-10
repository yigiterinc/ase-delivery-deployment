package com.group5.apigateway;

import com.group5.apigateway.config.AuthenticationFilter;
import com.group5.apigateway.config.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableEurekaClient
public class ApiGatewayApplication {

    @Autowired
    AuthenticationFilter filter;

    public static void main(String[] args) {
        SpringApplication.run(ApiGatewayApplication.class, args);
    }

    @Bean
    public RouteLocator routeLocator(RouteLocatorBuilder builder) {

        return builder.routes().route(routeSpec ->
                routeSpec.path("/api/ds/**")
                        .filters(f -> f.filter(filter))
                        .uri("lb://DELIVERY-SERVICE"))
                .route(routeSpec ->
                        routeSpec.path("/api/cas/**")
                        .filters(f -> f.filter(filter))
                        .uri("lb://CUSTOMER-AUTHENTICATION-SERVICE")
                ).build();
}

}
