package com.example.sso.responses;

import java.util.Map;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Component
public class GenericResponse {

    private boolean success;
    private String message;
    private Map<String, String> errors;
    private Map<String, Object> data;
}
