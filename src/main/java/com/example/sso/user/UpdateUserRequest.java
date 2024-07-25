package com.example.sso.user;

import java.time.LocalDate;
import java.util.List;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern.Flag;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class UpdateUserRequest {

    private String name;

    @Email(message = "The email address is invalid.", flags = {Flag.CASE_INSENSITIVE})
    private String email;

    private String password;

    private LocalDate dob;

    private List<Integer> roleIds;

    public UpdateUserRequest(
            String name,
            String email,
            String password,
            LocalDate dob,
            List<Integer> roleIds) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.dob = dob;
        this.roleIds = roleIds;
    }
}
