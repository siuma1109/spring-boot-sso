package com.example.sso.user;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
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

@RestController
@RequestMapping("api/v1/users")
public class UserController {

    private final UserService userService;
    private final GenericResponse genericResponse;

    public UserController(UserService userService, GenericResponse genericResponse) {
        this.userService = userService;
        this.genericResponse = genericResponse;
    }

    @GetMapping
    public List<User> getUsers() {
        return this.userService.getUsers();
    }

    @PostMapping
    public ResponseEntity<GenericResponse> registerNewUser(@Valid @RequestBody RegisterUserDto registerUserDto) {
        User user = this.userService.addNewUser(registerUserDto);
        Map<String, Object> userData = UserUtils.convertUserToMap(user);

        this.genericResponse.setData(userData);
        return ResponseEntity.ok(this.genericResponse);
    }

    @PutMapping("{userId}")
    public User updateUser(
            @PathVariable("userId") Long id,
            @Valid @RequestBody UpdateUserDto updateUserDto) {
        return userService.updateUser(id, updateUserDto);
    }

    @DeleteMapping("{userId}")
    public void deleteUser(@PathVariable("userId") Long id) {
        this.userService.deleteUser(id);
    }

}
