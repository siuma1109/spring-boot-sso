package com.example.sso.user;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.sso.responses.GenericResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('ADMIN') || #id == principal.id")
@RequestMapping("api/v1/users")
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<GenericResponse> getUsers() {
        List<UserDTO> users = this.userService.getUsers();
        return ResponseEntity.ok(
                GenericResponse.builder()
                        .success(true)
                        .data(UserUtils.convertUsersToMap(users))
                        .build()
        );
    }

    @PostMapping
    public ResponseEntity<GenericResponse> newUser(@Valid @RequestBody NewUserRequest newUserRequest) {
        User user = this.userService.addNewUser(newUserRequest);
        return ResponseEntity.ok(
                GenericResponse.builder()
                        .success(true)
                        .data(UserUtils.convertUserToMap(user))
                        .build()
        );
    }

    @PutMapping("{userId}")
    public ResponseEntity<GenericResponse> updateUser(
            @PathVariable("userId") Long id,
            @Valid @RequestBody UpdateUserRequest updateUserRequest) {
        User user = userService.updateUser(id, updateUserRequest);
        return ResponseEntity.ok(
                GenericResponse.builder()
                        .success(true)
                        .data(UserUtils.convertUserToMap(user))
                        .build()
        );
    }

    @DeleteMapping("{userId}")
    public ResponseEntity<GenericResponse> deleteUser(@PathVariable("userId") Long id) {
        this.userService.deleteUser(id);
        return ResponseEntity.ok(
                GenericResponse.builder()
                        .success(true)
                        .build()
        );
    }

}
