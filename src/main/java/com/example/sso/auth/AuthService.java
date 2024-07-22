package com.example.sso.auth;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.sso.exceptions.ValidationException;
import com.example.sso.jwt.JwtService;
import com.example.sso.responses.GenericResponse;
import com.example.sso.user.Role;
import com.example.sso.user.User;
import com.example.sso.user.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public GenericResponse register(RegisterRequest registerRequest) {
        User user = User.builder()
                .name(registerRequest.getName())
                .email(registerRequest.getEmail())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .dob(registerRequest.getDob())
                .role(Role.USER)
                .build();

        Map<String, String> errors = new HashMap<>();
        Optional<User> userByEmail = userRepository.findByEmail(user.getEmail());

        if (userByEmail.isPresent()) {
            errors.put("email", "Email is already taken");
        }

        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }

        userRepository.save(user);

        String jwtToken = jwtService.generateToken(user);

        return convertToGenericResponse(jwtToken);
    }

    public GenericResponse login(AuthRequest authRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authRequest.getEmail(),
                        authRequest.getPassword()
                )
        );

        User user = userRepository.findByEmail(authRequest.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        String jwtToken = jwtService.generateToken(user);

        return convertToGenericResponse(jwtToken);
    }

    private GenericResponse convertToGenericResponse(String token) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("token", token);

        return GenericResponse
                .builder()
                .success(true)
                .data(data)
                .build();
    }
}
