package com.group5.deliveryservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Box is already in use by another delivery")
public class BoxAlreadyFullException extends RuntimeException {
    public BoxAlreadyFullException() {
        super("Box contains delivery(s) of another customer!");
    }
}
