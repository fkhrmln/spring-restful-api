package com.example.app.controller;

import com.example.app.dto.AccessTokenResponse;
import com.example.app.dto.LoginRequest;
import com.example.app.dto.Response;
import com.example.app.entity.User;
import com.example.app.repository.UserRepository;
import com.example.app.security.BCrypt;
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
public class AuthControllerTest {

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
    void testLogin() throws Exception {
        User user = User.builder()
                .username("test")
                .password(BCrypt.hashpw("test123", BCrypt.gensalt()))
                .name("Test")
                .build();

        userRepository.save(user);

        LoginRequest request = LoginRequest.builder()
                .username("test")
                .password("test123")
                .build();

        mockMvc.perform(
                post("/api/auth/login")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(
                status().isOk(),
                result -> {
                    Response<AccessTokenResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
                    });

                    Assertions.assertNotNull(response.getData());

                    User usr = userRepository.findById("test").orElse(null);

                    Assertions.assertNotNull(usr.getAccessToken());
                    Assertions.assertNotNull(usr.getExpiredAt());
                }
        );
    }

    @Test
    void testLoginUsernameNotRegistered() throws Exception {
        LoginRequest request = LoginRequest.builder()
                .username("test")
                .password("test123")
                .build();

        mockMvc.perform(
                post("/api/auth/login")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(
                status().isUnauthorized(),
                result -> {
                    Response<Object> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
                    });

                    Assertions.assertEquals("Username Not Registered", response.getErrors());
                }
        );
    }

    @Test
    void testLoginWrongPassword() throws Exception {
        User user = User.builder()
                .username("test")
                .password(BCrypt.hashpw("test123", BCrypt.gensalt()))
                .name("Test")
                .build();

        userRepository.save(user);

        LoginRequest request = LoginRequest.builder()
                .username("test")
                .password("test")
                .build();

        mockMvc.perform(
                post("/api/auth/login")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(
                status().isUnauthorized(),
                result -> {
                    Response<AccessTokenResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
                    });

                    Assertions.assertEquals("Wrong Password", response.getErrors());
                }
        );
    }

    @Test
    void testLogout() throws Exception {
        User user = User.builder()
                .username("test")
                .password(BCrypt.hashpw("test123", BCrypt.gensalt()))
                .name("Test")
                .accessToken("12345")
                .expiredAt(System.currentTimeMillis() + (1000 * 60 * 24 * 7))
                .build();

        userRepository.save(user);

        mockMvc.perform(
                delete("/api/auth/logout")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-API-TOKEN", user.getAccessToken())
        ).andExpectAll(
                status().isOk(),
                result -> {
                    Response<Object> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
                    });

                    User logoutedUser = userRepository.findById(user.getUsername()).orElse(null);

                    Assertions.assertEquals("Logout Successfully", response.getMessage());
                    Assertions.assertNull(logoutedUser.getAccessToken());
                    Assertions.assertNull(logoutedUser.getExpiredAt());
                }
        );
    }

    @Test
    void testLogoutUnauthorized() throws Exception {
        mockMvc.perform(
                delete("/api/auth/logout")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                status().isUnauthorized(),
                result -> {
                    Response<Object> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
                    });

                    Assertions.assertNotNull(response.getErrors());
                }
        );
    }

}
