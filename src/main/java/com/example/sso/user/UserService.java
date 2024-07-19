package com.example.sso.user;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.example.sso.exceptions.ValidationException;

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
        Map<String, String> errors = new HashMap<>();
        Optional<User> userByEmail = userRepository.findUserByEmail(registerUserDto.getEmail());

        if (userByEmail.isPresent()) {
            errors.put("email", "Email is already taken");
        }

        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
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
