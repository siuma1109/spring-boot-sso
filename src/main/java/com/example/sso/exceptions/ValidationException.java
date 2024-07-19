package com.example.sso.exceptions;

import java.util.HashMap;
import java.util.Map;

public class ValidationException extends RuntimeException {
    private final Map<String, String> errors;

    public ValidationException(Map<String, String> errors) {
        super("Validation failed");
        this.errors = errors != null ? errors : new HashMap<>();
    }

    public Map<String, String> getErrors() {
        return errors;
    }
}