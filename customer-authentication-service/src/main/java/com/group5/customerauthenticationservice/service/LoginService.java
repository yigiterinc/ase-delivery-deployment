package com.group5.customerauthenticationservice.service;

import com.group5.customerauthenticationservice.exception.IncorrectCredentialsException;
import com.group5.customerauthenticationservice.config.JwtCreator;
import com.group5.customerauthenticationservice.model.Jwt;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class LoginService {
    private final JwtCreator jwtCreator;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public LoginService(JwtCreator jwtCreator,
                        UserService userService,
                        PasswordEncoder passwordEncoder) {

        this.jwtCreator = jwtCreator;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    public Jwt login(String email, String password) {
        UserDetails userDetails = userService.loadUserByUsername(email);

        if (!passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new IncorrectCredentialsException();
        }

        Map<String, String> claims = new HashMap<>();
        claims.put("username", email);

        String authorities = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        claims.put("authorities", authorities);
        claims.put("userId", String.valueOf(1));

        return jwtCreator.createJwtForClaims(email, claims);
    }
}
