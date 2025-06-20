package com.phoenixcode.Expense.Tracker.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> IllegalArgumentExceptionHandler(IllegalArgumentException ex) {
        ErrorResponse response = createResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<String> UserAlreadyExistsExceptionHandler(UserAlreadyExistsException ex) {
        ErrorResponse response = createResponse(HttpStatus.CONFLICT, ex.getMessage());
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
    }

    private ErrorResponse createResponse(HttpStatus httpStatus, String message) {

        return ErrorResponse.builder()
                .errorCode(httpStatus.value())
                .message(message)
                .timestamp(LocalDateTime.now())
                .build();
    }
}
