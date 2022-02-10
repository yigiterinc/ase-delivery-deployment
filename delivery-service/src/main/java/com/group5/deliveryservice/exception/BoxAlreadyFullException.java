package com.group5.deliveryservice.exception;

public class BoxAlreadyFullException extends RuntimeException {
    public BoxAlreadyFullException() {
        super("Box contains delivery(s) of another customer!");
    }
}
