package com.group5.customerauthenticationservice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.group5.customerauthenticationservice.dto.CreateUserDto;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.Email;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
@Document(collection = "users")
public class ASEDeliveryUser {

    public ASEDeliveryUser(Role role) {
        this.role = role;
    }

    public ASEDeliveryUser(final String email, final Role role, final String password) {
        this.email = email;
        this.role = role;
        this.password = password;
    }

    public ASEDeliveryUser(final CreateUserDto createUserDto) {
        this.email = createUserDto.getEmail();
        this.password = createUserDto.getPassword();
        this.role = createUserDto.getRole();
    }

    @Id
    private String id;

    @Email
    @Indexed(unique = true)
    private String email;

    private Role role;

    @JsonIgnore
    private String password;

    @Indexed(unique = true)
    private String rfid;
}
