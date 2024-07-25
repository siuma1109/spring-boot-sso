package com.example.sso.auth;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.sso.exceptions.ValidationException;
import com.example.sso.jwt.JwtService;
import com.example.sso.responses.GenericResponse;
import com.example.sso.role.RoleService;
import com.example.sso.user.User;
import com.example.sso.user.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public GenericResponse register(RegisterRequest registerRequest) {
        User user = User.builder()
                .name(registerRequest.getName())
                .email(registerRequest.getEmail())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .dob(registerRequest.getDob())
                .roles(roleService.getUserRoles())
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
        Map<String, String> errors = new HashMap<>();
        Optional<User> user = userRepository.findByEmail(authRequest.getEmail());

        if (!user.isPresent()) {
            errors.put("email", "User not found");
        }

        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authRequest.getEmail(),
                            authRequest.getPassword()
                    )
            );
        } catch (BadCredentialsException e) {
            errors.put("password", "Password incorrect");
            throw new ValidationException(errors);
        }

        String jwtToken = jwtService.generateToken(user.get());

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

    public boolean isAdmin() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth != null && auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ADMIN"));
    }
}
