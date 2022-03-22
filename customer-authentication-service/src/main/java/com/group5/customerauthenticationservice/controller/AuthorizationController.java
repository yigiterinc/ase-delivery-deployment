package com.group5.customerauthenticationservice.controller;

import com.group5.customerauthenticationservice.model.Role;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/authorize")
public class AuthorizationController {

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public Role authorize(Authentication jwtToken) {
        JwtAuthenticationToken token = (JwtAuthenticationToken) jwtToken;
        Map<String, Object> attributes = token.getTokenAttributes();

        return Role.valueOf(((String) attributes.get("authorities")).split("_")[1]);
    }
}
