package com.group5.deliveryservice.exception;

public class NoDeliveriesToDepositException extends RuntimeException {
    public NoDeliveriesToDepositException() {
        super("No deliveries for delivererId and boxId to deposit!");
    }
}
