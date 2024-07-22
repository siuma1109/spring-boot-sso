package com.example.sso.auth;

import java.time.LocalDate;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern.Flag;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

    @NotEmpty(message = "The name is required.")
    private String name;

    @NotEmpty(message = "The email is required.")
    @Email(message = "The email address is invalid.", flags = {Flag.CASE_INSENSITIVE})
    private String email;

    @NotEmpty(message = "The password is required.")
    private String password;

    private LocalDate dob;
}
