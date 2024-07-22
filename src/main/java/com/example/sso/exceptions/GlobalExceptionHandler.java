package com.example.sso.exceptions;

import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.example.sso.responses.GenericResponse;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<GenericResponse> handleAllExceptions(Exception ex) {
        return new ResponseEntity<>(
                GenericResponse
                        .builder()
                        .success(false)
                        .message(ex.getMessage())
                        .build(),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<GenericResponse> handleAllExceptions(ValidationException ex) {
        return new ResponseEntity<>(
                GenericResponse
                        .builder()
                        .success(false)
                        .message(ex.getMessage())
                        .errors(ex.getErrors())
                        .build(),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<GenericResponse> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        Map<String, String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));
        return new ResponseEntity<>(
                GenericResponse
                        .builder()
                        .success(false)
                        .message(ex.getMessage())
                        .errors(errors)
                        .build(),
                HttpStatus.BAD_REQUEST
        );
    }
}
