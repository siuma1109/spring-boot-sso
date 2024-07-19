package com.example.sso.user;

import java.time.LocalDate;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserConfig {

    @Bean
    CommandLineRunner commandLineRunner(UserRepository repository) {
        return args -> {
            User marco = new User(
                    "marco",
                    "marco@example.com",
                    "1234",
                    LocalDate.of(1998, 11, 9)
            );

            User apple = new User(
                    "apple",
                    "apple@example.com",
                    "1234",
                    LocalDate.of(2004, 11, 9)
            );

            repository.saveAll(
                List.of(marco, apple)
            );
        };
    }
}
