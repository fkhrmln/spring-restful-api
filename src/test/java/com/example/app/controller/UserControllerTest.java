package com.example.app.controller;

import com.example.app.dto.*;
import com.example.app.entity.User;
import com.example.app.repository.UserRepository;
import com.example.app.security.BCrypt;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.MockMvcBuilder.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @Test
    void testRegister() throws Exception {
        RegisterRequest request = RegisterRequest.builder()
                .username("test")
                .password("test123")
                .name("Test")
                .build();

        mockMvc.perform(
                post("/api/users")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(
                status().isCreated(),
                result -> {
                    Response<Object> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
                    });

                    Assertions.assertEquals("Register Successfully", response.getMessage());
                }
        );
    }

    @Test
    void testRegisterBadRequest() throws Exception {
        RegisterRequest request = RegisterRequest.builder()
                .username("")
                .password("")
                .name("")
                .build();

        mockMvc.perform(
                post("/api/users")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(
                status().isBadRequest(),
                result -> {
                    Response<Object> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
                    });

                    Assertions.assertNotNull(response.getErrors());
                }
        );
    }

    @Test
    void testRegisterUsernameAlreadyExists() throws Exception {
        User user = User.builder()
                .username("test")
                .password(BCrypt.hashpw("test123", BCrypt.gensalt()))
                .name("Test")
                .build();

        userRepository.save(user);

        RegisterRequest request = RegisterRequest.builder()
                .username("test")
                .password("test123")
                .name("Test")
                .build();

        mockMvc.perform(
                post("/api/users")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(
                status().isConflict(),
                result -> {
                    Response<Object> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
                    });

                    Assertions.assertEquals("Username Already Exists", response.getErrors());
                }
        );
    }

    @Test
    void testCurrentUser() throws Exception {
        User user = User.builder()
                .username("test")
                .password(BCrypt.hashpw("test123", BCrypt.gensalt()))
                .name("Test")
                .accessToken("12345")
                .expiredAt(System.currentTimeMillis() + (1000 * 60 * 24 * 7))
                .build();

        userRepository.save(user);

        mockMvc.perform(
                get("/api/users/current")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-API-TOKEN", user.getAccessToken())
        ).andExpectAll(
                status().isOk(),
                result -> {
                    Response<UserResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
                    });

                    Assertions.assertNotNull(response.getData());
                    Assertions.assertEquals(user.getUsername(), response.getData().getUsername());
                    Assertions.assertEquals(user.getName(), response.getData().getName());
                }
        );
    }

    @Test
    void testCurrentUserUnauthorized() throws Exception {
        mockMvc.perform(
                get("/api/users/current")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                status().isUnauthorized(),
                result -> {
                    Response<UserResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
                    });

                    Assertions.assertNotNull(response.getErrors());
                }
        );
    }

    @Test
    void testUpdate() throws Exception {
        User user = User.builder()
                .username("test")
                .password(BCrypt.hashpw("test123", BCrypt.gensalt()))
                .name("Test")
                .accessToken("12345")
                .expiredAt(System.currentTimeMillis() + (1000 * 60 * 24 * 7))
                .build();

        userRepository.save(user);

        UpdateUserRequest request = UpdateUserRequest.builder()
                .name("Updated Test")
                .build();

        mockMvc.perform(
                patch("/api/users/current")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-API-TOKEN", user.getAccessToken())
                        .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(
                status().isOk(),
                result -> {
                    Response<UserResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
                    });

                    User updatedUser = userRepository.findById(user.getName()).orElse(null);

                    Assertions.assertNotNull(response.getData());
                    Assertions.assertEquals(request.getName(), response.getData().getName());
                    Assertions.assertEquals("Updated Test", updatedUser.getName());
                }
        );
    }

    @Test
    void testUpdateUnauthorized() throws Exception {
        mockMvc.perform(
                patch("/api/users/current")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                status().isUnauthorized(),
                result -> {
                    Response<UserResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
                    });

                    Assertions.assertNotNull(response.getErrors());
                }
        );
    }

}
