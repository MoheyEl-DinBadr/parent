package com.mohey.basemodule.exception;

import lombok.extern.java.Log;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpServerErrorException;

import java.util.logging.Level;

@Order(Ordered.HIGHEST_PRECEDENCE)
@Log
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<Object> handleValidationException(ValidationException validationException) {
        log.log(Level.WARNING, validationException.getMessage());
        return new ResponseEntity<>(validationException, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpServerErrorException.class)
    public ResponseEntity<Object> handleHttpServerError(HttpServerErrorException ex) {
        return new ResponseEntity<>(ex.getMessage(), ex.getStatusCode());
    }

    @ExceptionHandler(Throwable.class)
    public ResponseEntity<Object> handleException(Throwable throwable) {
        log.log(Level.SEVERE, "Handled Error Message: ", throwable);
        return new ResponseEntity<>(throwable.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
