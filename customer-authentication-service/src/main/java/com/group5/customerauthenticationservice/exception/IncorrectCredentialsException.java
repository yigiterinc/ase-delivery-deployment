package com.group5.customerauthenticationservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Incorrect credentials")
public class IncorrectCredentialsException extends RuntimeException {
}
