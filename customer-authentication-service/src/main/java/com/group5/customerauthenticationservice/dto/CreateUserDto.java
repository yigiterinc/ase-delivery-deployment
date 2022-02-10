package com.group5.customerauthenticationservice.dto;

import com.group5.customerauthenticationservice.model.Role;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateUserDto {
    private String email;
    private String password;
    private Role role;
}