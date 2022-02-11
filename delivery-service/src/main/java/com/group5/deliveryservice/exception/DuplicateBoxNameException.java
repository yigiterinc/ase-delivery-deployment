package com.group5.deliveryservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Duplicate box name")
public class DuplicateBoxNameException extends RuntimeException {
}
