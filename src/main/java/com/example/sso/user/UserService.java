package com.example.sso.user;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.example.sso.exceptions.ValidationException;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public List<UserDTO> getUsers() {
        List<User> users = userRepository.findAll();
        return users.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    private UserDTO convertToDTO(User user) {
        return new UserDTO(user.getId(), user.getName(), user.getEmail(), user.getDob(), user.getAge(), user.getRole());
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).get();
    }

    public User addNewUser(NewUserRequest newUserRequest) {
        Map<String, String> errors = new HashMap<>();
        Optional<User> userByEmail = userRepository.findByEmail(newUserRequest.getEmail());

        if (userByEmail.isPresent()) {
            errors.put("email", "Email is already taken");
        }

        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }

        User user = User.builder()
                .name(newUserRequest.getName())
                .email(newUserRequest.getEmail())
                .password(passwordEncoder.encode(newUserRequest.getPassword()))
                .dob(newUserRequest.getDob())
                .role(Role.USER)
                .build();

        return userRepository.save(user);
    }

    @Transactional
    public User updateUser(Long id, UpdateUserRequest updateUserRequest) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("student with id " + id + " does not exists."));

        String name = updateUserRequest.getName();
        if (name != null && name.length() > 0 && !Objects.equals(user.getName(), name)) {
            user.setName(name);
        }

        String email = updateUserRequest.getEmail();
        if (email != null && email.length() > 0 && !Objects.equals(user.getEmail(), email)) {
            Optional<User> userByEmail = userRepository.findByEmail(email);
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
