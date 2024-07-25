package com.example.sso.user;

import java.time.LocalDate;
import java.util.List;

import com.example.sso.role.Role;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

    private Long id;
    private String name;
    private String email;
    private LocalDate dob;
    private Integer age;
    private List<Role> role;
}
