package com.DreamFactory.DF.User;


import com.DreamFactory.DF.user.dto.UserRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    @Transactional
    @WithMockUser(roles = {"ADMIN"})
    void should_getAllUsers() throws Exception {
        mockMvc.perform(get("/api/users")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(7)))
                .andExpect(jsonPath("$.[0].username").value("admin"))
                .andExpect(jsonPath("$.[0].email").value("admin@happytravel.com"))
                .andExpect(jsonPath("$.[1].username").value("user"))
                .andExpect(jsonPath("$.[1].email").value("user@happytravel.com"))
                .andExpect(jsonPath("$.[2].username").value("john_doe"))
                .andExpect(jsonPath("$.[2].email").value("john@example.com"))
                .andExpect(jsonPath("$.[3].username").value("jane_smith"))
                .andExpect(jsonPath("$.[3].email").value("jane@example.com"))
                .andExpect(jsonPath("$.[4].username").value("mike_wilson"))
                .andExpect(jsonPath("$.[4].email").value("mike@example.com"))
                .andExpect(jsonPath("$.[5].username").value("sarah_jones"))
                .andExpect(jsonPath("$.[5].email").value("sarah@example.com"))
                .andExpect(jsonPath("$.[6].username").value("david_brown"))
                .andExpect(jsonPath("$.[6].email").value("david@example.com"));

    }

    @Test
    @Transactional
    void should_registerUser_fromRequest() throws Exception{
        UserRequest userRequest = new UserRequest("userTest", "usertest@test.com", "password123");

        mockMvc.perform(post("/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value("userTest"))
                .andExpect(jsonPath("$.email").value("usertest@test.com"))
                .andExpect(jsonPath("$.id").exists());
    }

    @Test
    @Transactional
    @WithMockUser(roles = {"ADMIN"})
    void should_updateUser_fromRequest() throws Exception{

        Long userId = 1L;
        UserRequest userRequest = new UserRequest("updateTest", "updatetest@test.com", "password123");

        mockMvc.perform(put("/api/users/{id}", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userRequest)))
                .andExpect(jsonPath("$.id").value(userId))
                .andExpect(jsonPath("$.username").value("updateTest"))
                .andExpect(jsonPath("$.email").value("updatetest@test.com"));
    }

}
