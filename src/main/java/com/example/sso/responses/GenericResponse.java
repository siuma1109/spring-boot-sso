package com.example.sso.responses;

import java.util.Map;
import java.util.Objects;

import io.micrometer.common.lang.Nullable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GenericResponse {

    private boolean success;
    private String message;
    private Map<String, String> errors;

    public GenericResponse empty() {
        return success();
    }

    public GenericResponse success() {
        return GenericResponse.builder()
                .message(Objects.nonNull(message) ? message : "SUCCESS!")
                .success(true)
                .build();
    }

    public GenericResponse error(@Nullable Map<String, String> errors) {
        return GenericResponse.builder()
                .message(Objects.nonNull(message) ? message : "ERROR!")
                .success(false)
                .errors(errors)
                .build();
    }
}
