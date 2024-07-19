package com.example.sso.user;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import jakarta.transaction.Transactional;

@Component
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getUsers() {
        return userRepository.findAll();
    }

    public User addNewUser(RegisterUserDto registerUserDto) {
        Optional<User> userByEmail = userRepository.findUserByEmail(registerUserDto.getEmail());

        if (userByEmail.isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email Taken");
        }

        return userRepository.save(new User(
                registerUserDto.getName(),
                registerUserDto.getEmail(),
                registerUserDto.getPassword(),
                registerUserDto.getDob()
        ));
    }

    @Transactional
    public User updateUser(Long id, UpdateUserDto updateUserDto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("student with id " + id + " does not exists."));

        String name = updateUserDto.getName();
        if (name != null && name.length() > 0 && !Objects.equals(user.getName(), name)) {
            user.setName(name);
        }

        String email = updateUserDto.getEmail();
        if (email != null && email.length() > 0 && !Objects.equals(user.getEmail(), email)) {
            Optional<User> userByEmail = userRepository.findUserByEmail(email);
            if (userByEmail.isPresent()) {
                throw new IllegalStateException("Email Taken");
            }
            user.setEmail(email);
        }

        return user;
    }

    public void deleteUser(Long id) {
        boolean exists = userRepository.existsById(id);

        if (!exists) {
            throw new IllegalStateException("User with id " + id + " does not exists");
        }

        userRepository.deleteById(id);
    }
}
