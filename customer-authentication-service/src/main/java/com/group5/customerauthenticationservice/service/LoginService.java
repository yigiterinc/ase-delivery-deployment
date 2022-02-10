package com.group5.customerauthenticationservice.service;

import com.group5.customerauthenticationservice.IncorrectCredentialsException;
import com.group5.customerauthenticationservice.config.JwtCreator;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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

    public String login(String email, String password) {
        // TODO: Refactor this method
        UserDetails userDetails;
        try {
            userDetails = userService.loadUserByUsername(email);
        } catch (UsernameNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found");
        }

        if (passwordEncoder.matches(password, userDetails.getPassword())) {
            Map<String, String> claims = new HashMap<>();
            claims.put("username", email);

            String authorities = userDetails.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.joining(","));
            claims.put("authorities", authorities);
            claims.put("userId", String.valueOf(1));

            return jwtCreator.createJwtForClaims(email, claims);
        }

        throw new IncorrectCredentialsException();
    }
}
