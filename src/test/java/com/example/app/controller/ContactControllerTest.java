package com.example.app.controller;

import com.example.app.dto.*;
import com.example.app.entity.Contact;
import com.example.app.entity.User;
import com.example.app.repository.ContactRepository;
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

import java.util.UUID;

import static org.springframework.test.web.servlet.MockMvcBuilder.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ContactControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        contactRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void testCreateContact() throws Exception {
        User user = User.builder()
                .username("test")
                .password(BCrypt.hashpw("test123", BCrypt.gensalt()))
                .name("Test")
                .accessToken("12345")
                .expiredAt(System.currentTimeMillis() + (1000 * 60 * 24 * 7))
                .build();

        userRepository.save(user);

        CreateContactRequest request = CreateContactRequest.builder()
                .name("Test Contact")
                .phone("08123456789")
                .email("example@gmail.com")
                .build();

        mockMvc.perform(
                post("/api/contacts")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-API-TOKEN", user.getAccessToken())
                        .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(
                status().isCreated(),
                result -> {
                    Response<ContactResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
                    });

                    Assertions.assertEquals("Contact Created Successfully", response.getMessage());
                    Assertions.assertNotNull(response.getData().getId());
                }
        );
    }

    @Test
    void testCreateContactBadRequest() throws Exception {
        User user = User.builder()
                .username("test")
                .password(BCrypt.hashpw("test123", BCrypt.gensalt()))
                .name("Test")
                .accessToken("12345")
                .expiredAt(System.currentTimeMillis() + (1000 * 60 * 24 * 7))
                .build();

        userRepository.save(user);

        CreateContactRequest request = CreateContactRequest.builder()
                .phone("08123456789")
                .email("example@gmail.com")
                .build();

        mockMvc.perform(
                post("/api/contacts")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-API-TOKEN", user.getAccessToken())
                        .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(
                status().isBadRequest(),
                result -> {
                    Response<ContactResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
                    });

                    Assertions.assertNotNull(response.getErrors());
                }
        );
    }

    @Test
    void testCreateContactUnauthorized() throws Exception {
        mockMvc.perform(
                post("/api/contacts")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                status().isUnauthorized(),
                result -> {
                    Response<ContactResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
                    });

                    Assertions.assertNotNull(response.getErrors());
                }
        );
    }

    @Test
    void testGetContact() throws Exception {
        User user = User.builder()
                .username("test")
                .password(BCrypt.hashpw("test123", BCrypt.gensalt()))
                .name("Test")
                .accessToken("12345")
                .expiredAt(System.currentTimeMillis() + (1000 * 60 * 24 * 7))
                .build();

        userRepository.save(user);

        Contact contact = Contact.builder()
                .id("12345")
                .name("Test Contact")
                .phone("08123456789")
                .email("example@gmail.com")
                .user(user)
                .build();

        contactRepository.save(contact);

        mockMvc.perform(
                get("/api/contacts/12345")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-API-TOKEN", "12345")
        ).andExpectAll(
                status().isOk(),
                result -> {
                    Response<ContactResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
                    });

                    Assertions.assertNotNull(response.getData());
                    Assertions.assertEquals("Test Contact", response.getData().getName());
                }
        );
    }

    @Test
    void testGetContactNotFound() throws Exception {
        User user = User.builder()
                .username("test")
                .password(BCrypt.hashpw("test123", BCrypt.gensalt()))
                .name("Test")
                .accessToken("12345")
                .expiredAt(System.currentTimeMillis() + (1000 * 60 * 24 * 7))
                .build();

        userRepository.save(user);

        mockMvc.perform(
                get("/api/contacts/12345")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-API-TOKEN", "12345")
        ).andExpectAll(
                status().isNotFound(),
                result -> {
                    Response<ContactResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
                    });

                    Assertions.assertEquals("Contact Not Found", response.getErrors());
                }
        );
    }

    @Test
    void testGetContactUnauthorized() throws Exception {
        mockMvc.perform(
                get("/api/contacts/12345")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                status().isUnauthorized(),
                result -> {
                    Response<ContactResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
                    });

                    Assertions.assertNotNull(response.getErrors());
                }
        );
    }

    @Test
    void testUpdateContact() throws Exception {
        User user = User.builder()
                .username("test")
                .password(BCrypt.hashpw("test123", BCrypt.gensalt()))
                .name("Test")
                .accessToken("12345")
                .expiredAt(System.currentTimeMillis() + (1000 * 60 * 24 * 7))
                .build();

        userRepository.save(user);

        Contact contact = Contact.builder()
                .id("12345")
                .name("Test Contact")
                .phone("08123456789")
                .email("example@gmail.com")
                .user(user)
                .build();

        contactRepository.save(contact);

        UpdateContactRequest request = UpdateContactRequest.builder()
                .id("12345")
                .name("Test Contact Updated")
                .phone("08123456789")
                .email("example@gmail.com")
                .build();

        mockMvc.perform(
                put("/api/contacts/12345")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-API-TOKEN", user.getAccessToken())
                        .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(
                status().isOk(),
                result -> {
                    Response<ContactResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
                    });

                    Assertions.assertEquals("Contact Updated Successfully", response.getMessage());
                    Assertions.assertEquals("Test Contact Updated", response.getData().getName());
                }
        );
    }

    @Test
    void testUpdateContactNotFound() throws Exception {
        User user = User.builder()
                .username("test")
                .password(BCrypt.hashpw("test123", BCrypt.gensalt()))
                .name("Test")
                .accessToken("12345")
                .expiredAt(System.currentTimeMillis() + (1000 * 60 * 24 * 7))
                .build();

        userRepository.save(user);

        UpdateContactRequest request = UpdateContactRequest.builder()
                .id("12345")
                .name("Test Contact")
                .phone("08123456789")
                .email("example@gmail.com")
                .build();

        mockMvc.perform(
                put("/api/contacts/12345")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-API-TOKEN", "12345")
                        .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(
                status().isNotFound(),
                result -> {
                    Response<ContactResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
                    });

                    Assertions.assertEquals("Contact Not Found", response.getErrors());
                }
        );
    }

    @Test
    void testUpdateContactUnauthorized() throws Exception {
        mockMvc.perform(
                put("/api/contacts/12345")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                status().isUnauthorized(),
                result -> {
                    Response<ContactResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
                    });

                    Assertions.assertNotNull(response.getErrors());
                }
        );
    }

    @Test
    void testRemoveContact() throws Exception {
        User user = User.builder()
                .username("test")
                .password(BCrypt.hashpw("test123", BCrypt.gensalt()))
                .name("Test")
                .accessToken("12345")
                .expiredAt(System.currentTimeMillis() + (1000 * 60 * 24 * 7))
                .build();

        userRepository.save(user);

        Contact contact = Contact.builder()
                .id("12345")
                .name("Test Contact")
                .phone("08123456789")
                .email("example@gmail.com")
                .user(user)
                .build();

        contactRepository.save(contact);

        mockMvc.perform(
                delete("/api/contacts/12345")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-API-TOKEN", user.getAccessToken())
        ).andExpectAll(
                status().isOk(),
                result -> {
                    Response<Object> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
                    });

                    Assertions.assertEquals("Contact Deleted Successfully", response.getMessage());
                    Assertions.assertNull(userRepository.findById("12345").orElse(null));
                }
        );
    }

    @Test
    void testRemoveContactNotFound() throws Exception {
        User user = User.builder()
                .username("test")
                .password(BCrypt.hashpw("test123", BCrypt.gensalt()))
                .name("Test")
                .accessToken("12345")
                .expiredAt(System.currentTimeMillis() + (1000 * 60 * 24 * 7))
                .build();

        userRepository.save(user);

        mockMvc.perform(
                delete("/api/contacts/12345")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-API-TOKEN", user.getAccessToken())
        ).andExpectAll(
                status().isNotFound(),
                result -> {
                    Response<Object> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
                    });

                    Assertions.assertEquals("Contact Not Found", response.getErrors());
                }
        );
    }

    @Test
    void testRemoveContactUnauthorized() throws Exception {
        mockMvc.perform(
                delete("/api/contacts/12345")
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

    @Test
    void testSearchContacts() throws Exception {
        User user = User.builder()
                .username("test")
                .password(BCrypt.hashpw("test123", BCrypt.gensalt()))
                .name("Test")
                .accessToken("12345")
                .expiredAt(System.currentTimeMillis() + (1000 * 60 * 24 * 7))
                .build();

        userRepository.save(user);

        for (int i = 1; i <= 3; i++) {
            Contact contact = Contact.builder()
                    .id(UUID.randomUUID().toString())
                    .name(STR."Test Contact \{i}")
                    .phone("08123456789")
                    .email("example@gmail.com")
                    .user(user)
                    .build();

            contactRepository.save(contact);
        }

        mockMvc.perform(
                get("/api/contacts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-API-TOKEN", user.getAccessToken())
                        .queryParam("name", "Test")
        ).andExpectAll(
                status().isOk(),
                result -> {
                    Response<SearchContactResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
                    });

                    Assertions.assertEquals(3, response.getData().getContacts().size());
                    Assertions.assertEquals(0, response.getData().getPage().getCurrentPage());
                    Assertions.assertEquals(1, response.getData().getPage().getTotalPage());
                    Assertions.assertEquals(10, response.getData().getPage().getSize());
                }
        );
    }

    @Test
    void testSearchContactsEmpty() throws Exception {
        User user = User.builder()
                .username("test")
                .password(BCrypt.hashpw("test123", BCrypt.gensalt()))
                .name("Test")
                .accessToken("12345")
                .expiredAt(System.currentTimeMillis() + (1000 * 60 * 24 * 7))
                .build();

        userRepository.save(user);

        mockMvc.perform(
                get("/api/contacts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-API-TOKEN", user.getAccessToken())
                        .queryParam("name", "Test")
        ).andExpectAll(
                status().isOk(),
                result -> {
                    Response<SearchContactResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
                    });

                    Assertions.assertEquals(0, response.getData().getContacts().size());
                    Assertions.assertEquals(0, response.getData().getPage().getCurrentPage());
                    Assertions.assertEquals(0, response.getData().getPage().getTotalPage());
                    Assertions.assertEquals(10, response.getData().getPage().getSize());
                }
        );
    }

}
