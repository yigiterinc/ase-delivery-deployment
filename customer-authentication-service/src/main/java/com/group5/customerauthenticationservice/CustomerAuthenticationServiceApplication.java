package com.group5.customerauthenticationservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class CustomerAuthenticationServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(CustomerAuthenticationServiceApplication.class, args);
    }

}
