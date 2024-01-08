package com.example.app.controller;

import com.example.app.dto.Response;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice
public class ExceptionController {

    @ExceptionHandler(value = ConstraintViolationException.class)
    public ResponseEntity<Response<?>> constraintViolationException(ConstraintViolationException exception) {
        Response<Object> response = Response.builder()
                .status("FAIL")
                .errors(exception.getMessage())
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(value = ResponseStatusException.class)
    public ResponseEntity<Response<?>> apiException(ResponseStatusException exception) {
        Response<Object> response = Response.builder()
                .status("FAIL")
                .errors(exception.getReason())
                .build();

        return ResponseEntity.status(exception.getStatusCode()).body(response);
    }

}
