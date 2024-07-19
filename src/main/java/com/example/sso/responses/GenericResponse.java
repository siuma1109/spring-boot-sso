package com.example.sso.responses;

import java.util.Map;
import java.util.Objects;

import org.springframework.stereotype.Component;

import io.micrometer.common.lang.Nullable;
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

    public GenericResponse empty() {
        return success();
    }

    public GenericResponse success() {
        return GenericResponse.builder()
                .success(true)
                .message(Objects.nonNull(message) ? message : "SUCCESS!")
                .data(data)
                .build();
    }

    public GenericResponse error(@Nullable Map<String, String> errors) {
        return GenericResponse.builder()
                .success(false)
                .message(Objects.nonNull(message) ? message : "ERROR!")
                .data(data)
                .errors(errors)
                .build();
    }
}
