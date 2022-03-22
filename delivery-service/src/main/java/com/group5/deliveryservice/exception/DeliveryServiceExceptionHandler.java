package com.group5.deliveryservice.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class DeliveryServiceExceptionHandler {

    @ExceptionHandler(value = { BoxAlreadyFullException.class })
    public ResponseEntity<Object> handleInvalidInputException(BoxAlreadyFullException ex) {
        log.error("Box Already Full Exception: ", ex.getMessage());
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = { DuplicateBoxNameException.class })
    public ResponseEntity<Object> handleDuplicateBoxNameException(DuplicateBoxNameException ex) {
        log.error("Duplicate Box Name Exception: ", ex.getMessage());
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = { InvalidIdException.class })
    public ResponseEntity<Object> handleInvalidIdException(InvalidIdException ex) {
        log.error("Invalid Id Exception: ", ex.getMessage());
        return new ResponseEntity<>(ex.getMessage(),HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = { InvalidStatusChangeException.class })
    public ResponseEntity<Object> handleInvalidStatusChangeException(InvalidIdException ex) {
        log.error("Invalid Status Change Exception: ", ex.getMessage());
        return new ResponseEntity<>(ex.getMessage(),HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = { NoDeliveriesToDepositException.class })
    public ResponseEntity<Object> handleNoDeliveriesToDepositException(NoDeliveriesToDepositException ex) {
        log.error("No Deliveries to Deposit Exception: ", ex.getMessage());
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = { NotFoundException.class })
    public ResponseEntity<Object> handleNotFoundException(NotFoundException ex) {
        log.error("Not Found Exception: ", ex.getMessage());
        return new ResponseEntity<>(ex.getMessage(),HttpStatus.NOT_FOUND);
    }

}
