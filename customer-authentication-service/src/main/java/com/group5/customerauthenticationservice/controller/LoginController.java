package com.group5.customerauthenticationservice.controller;

import com.group5.customerauthenticationservice.dto.LoginDto;
import com.group5.customerauthenticationservice.model.Jwt;
import com.group5.customerauthenticationservice.service.LoginService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/login")
public class LoginController {

    final LoginService loginService;

    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @PostMapping
    public Jwt login(@RequestBody LoginDto loginDto) {

        return loginService.login(loginDto.getEmail(), loginDto.getPassword());
    }
}
