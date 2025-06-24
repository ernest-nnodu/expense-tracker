package com.phoenixcode.Expense.Tracker.exception;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException ex) {
        String errors = ex.getBindingResult().getFieldErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining(", "));

        ErrorResponse response = createResponse(HttpStatus.BAD_REQUEST, errors);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> UserAlreadyExistsExceptionHandler(UserAlreadyExistsException ex) {
        ErrorResponse response = createResponse(HttpStatus.CONFLICT, ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    private ErrorResponse createResponse(HttpStatus httpStatus, String message) {

        return ErrorResponse.builder()
                .errorCode(httpStatus.value())
                .message(message)
                .timestamp(LocalDateTime.now())
                .build();
    }
}
