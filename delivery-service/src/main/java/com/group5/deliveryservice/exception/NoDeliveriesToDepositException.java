package com.group5.deliveryservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "No delivery found")
public class NoDeliveriesToDepositException extends RuntimeException {
    public NoDeliveriesToDepositException() {
        super("No deliveries for delivererId and boxId to deposit!");
    }
}
