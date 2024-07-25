package com.example.sso.role;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NewRoleRequest {

    @NotEmpty(message = "The name is required.")
    private String name;

    private String description;
    private Boolean isDefault;
}
