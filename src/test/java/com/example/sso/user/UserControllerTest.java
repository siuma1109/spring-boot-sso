package com.example.sso.user;

import java.time.LocalDate;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    UserService userService;

    private User userAdmin;
    private User userUser;

    @BeforeEach
    void setUp() {
        userAdmin = userService.findByEmail("admin@example.com").get();
        userUser = userService.findByEmail("user@example.com").get();
    }

    @Test
    void getUsersBlockIfNotLogin() throws Exception {
        mockMvc.perform(get("/api/v1/users"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithUserDetails(value = "user@example.com")
    void getUsersBlockIfNotAdmin() throws Exception {
        mockMvc.perform(get("/api/v1/users")
                .with(SecurityMockMvcRequestPostProcessors.csrf())
        ).andExpect(status().isForbidden());
    }

    @Test
    @WithUserDetails(value = "admin@example.com")
    void getUsersSuccess() throws Exception {
        mockMvc.perform(get("/api/v1/users"))
                .andExpect(status().isOk());
    }

    @Test
    @WithUserDetails(value = "admin@example.com")
    void newUser() throws Exception {
        NewUserRequest request = NewUserRequest.builder()
                .name("New User")
                .email("new_user@example.com")
                .password("1234")
                .dob(LocalDate.parse("1998-01-01"))
                .build();

        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    @WithUserDetails(value = "user@example.com")
    void newUserFailWithUser() throws Exception {
        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithUserDetails(value = "user@example.com")
    void updateUserByOwn() throws Exception {
        UpdateUserRequest request = UpdateUserRequest.builder()
                .name("Updated user name")
                .build();

        mockMvc.perform(put("/api/v1/users/" + userUser.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.name").value("Updated user name"));
    }

    @Test
    @WithUserDetails(value = "admin@example.com")
    void updateUserByAdmin() throws Exception {
        UpdateUserRequest request = UpdateUserRequest.builder()
                .name("Updated user name")
                .build();

        mockMvc.perform(put("/api/v1/users/" + userUser.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.name").value("Updated user name"));
    }

    @Test
    @WithUserDetails(value = "user@example.com")
    @Transactional
    void deleteUserByOwn() throws Exception {
        mockMvc.perform(delete("/api/v1/users/" + userUser.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithUserDetails(value = "admin@example.com")
    @Transactional
    void deleteUserByAdmin() throws Exception {
        mockMvc.perform(delete("/api/v1/users/" + userUser.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithUserDetails(value = "user@example.com")
    @Transactional
    void deleteUserCannotDeleteOthers() throws Exception {
        mockMvc.perform(delete("/api/v1/users/" + userAdmin.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }
}
