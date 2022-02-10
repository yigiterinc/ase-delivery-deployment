package com.group5.deliveryservice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.Email;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "users")
public class User {
    @Transient
    public static final String SEQUENCE_NAME = "users_sequence";

    @Id
    private long id;

    @Email
    @Indexed(unique = true)
    private String email;

    @JsonIgnore
    private String password;

    private Role role;

    private String firstName;

    private String lastName;

    public User(Role role) {
        this.role = role;
    }

    public User(final String email, final String password, final Role role, final String firstName, final String lastName) {
        this.email = email;
        this.password = password;
        this.role = role;
        this.firstName = firstName;
        this.lastName = lastName;
    }
}
