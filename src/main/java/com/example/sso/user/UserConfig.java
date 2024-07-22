package com.example.sso.user;

import java.time.LocalDate;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Configuration
public class UserConfig {

    private final PasswordEncoder passwordEncoder;

    @Bean
    CommandLineRunner commandLineRunner(UserRepository repository) {
        return args -> {
            User marco = new User(
                    "marco",
                    "marco@example.com",
                    passwordEncoder.encode("1234"),
                    LocalDate.of(1998, 11, 9),
                    Role.ADMIN
            );

            User apple = new User(
                    "apple",
                    "apple@example.com",
                    passwordEncoder.encode("1234"),
                    LocalDate.of(2004, 11, 9),
                    Role.USER
            );

            repository.saveAll(
                List.of(marco, apple)
            );
        };
    }
}
