package com.example.sso.auth;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.example.sso.responses.GenericResponse;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AuthControllerTests {

    @Autowired
    private TestRestTemplate template;

    @Test
    void shouldRegisterUser() {
        RegisterRequest registerRequest = RegisterRequest.builder()
                .name("Test register success")
                .email("test_register@example.com")
                .password("password")
                .build();

        ResponseEntity<GenericResponse> response = template.postForEntity("/api/v1/auth/register", registerRequest, GenericResponse.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void shouldNotRegisterUser() {
        RegisterRequest registerRequest = RegisterRequest.builder()
                .name("Test register success")
                .password("password")
                .build();

        ResponseEntity<GenericResponse> response = template.postForEntity("/api/v1/auth/register", registerRequest, GenericResponse.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void shouldLoginUser() {
        AuthRequest authRequest = AuthRequest.builder()
                .email("marco@example.com")
                .password("1234")
                .build();

        ResponseEntity<GenericResponse> response = template.postForEntity("/api/v1/auth/login", authRequest, GenericResponse.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void shouldNotLoginUser() {
        AuthRequest authRequest = AuthRequest.builder()
                .email("marco@example.com")
                .password("12345")
                .build();

        ResponseEntity<GenericResponse> response = template.postForEntity("/api/v1/auth/login", authRequest, GenericResponse.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}
