package com.example.sso.user;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.example.sso.auth.AuthRequest;
import com.example.sso.auth.AuthService;
import com.example.sso.responses.GenericResponse;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerTests {

    @Autowired
    private TestRestTemplate template;
    @Autowired
    private AuthService authService;

    @Test
    void testGetUsersSuccessWithAuthToken() {
        // Arrange
        AuthRequest authRequest = AuthRequest.builder()
                .email("admin@example.com")
                .password("1234")
                .build();

        GenericResponse genericResponse = authService.login(authRequest);
        String bearerToken = (String) genericResponse.getData().get("token");

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(bearerToken);
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<GenericResponse> response = template
                .exchange(
                        "/api/v1/users",
                        HttpMethod.GET,
                        entity,
                        GenericResponse.class
                );

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testGetUsersFailWithAuthToken() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth("wrong token");
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<GenericResponse> response = template
                .exchange(
                        "/api/v1/users",
                        HttpMethod.GET,
                        entity,
                        GenericResponse.class
                );

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    void testOnlyAdminCanGetUsersWithAuthToken() {
        // Arrange
        AuthRequest authRequest = AuthRequest.builder()
                .email("user@example.com")
                .password("1234")
                .build();

        GenericResponse genericResponse = authService.login(authRequest);
        String bearerToken = (String) genericResponse.getData().get("token");

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(bearerToken);
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<GenericResponse> response = template
                .exchange(
                        "/api/v1/users",
                        HttpMethod.GET,
                        entity,
                        GenericResponse.class
                );

        // Assert
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

}
