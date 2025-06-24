package com.phoenixcode.Expense.Tracker.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.phoenixcode.Expense.Tracker.dto.CreateUserRequestDto;
import com.phoenixcode.Expense.Tracker.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;
    
    @BeforeEach
    void init() {
        userRepository.deleteAll();
    }
    
    @Test
    @DisplayName("Create user endpoint call with valid user credentials successful")
    void createUser_withValidCredentials_returnsUserAnd201StatusCode() throws Exception {
        CreateUserRequestDto userRequestDto = createUserRequestDto();

        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userRequestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.username").value("john_doe"))
                .andExpect(jsonPath("$.email").value("john@example.com"));
    }

    @Test
    @DisplayName("Create user endpoint call with existing username unsuccessful")
    void createUser_withExistingUsername_returns409StatusCode() throws Exception {
        CreateUserRequestDto userRequestDto = createUserRequestDto();
        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userRequestDto)));

        CreateUserRequestDto existingUser = CreateUserRequestDto.builder()
                .username("john_doe")
                .email("test@email.com")
                .password("pass")
                .build();

        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(existingUser)))
                .andExpect(status().isConflict());
    }

    @Test
    @DisplayName("Create user endpoint call with existing email unsuccessful")
    void createUser_withExistingEmail_returns409StatusCode() throws Exception {
        CreateUserRequestDto userRequestDto = createUserRequestDto();
        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userRequestDto)));

        CreateUserRequestDto existingUser = CreateUserRequestDto.builder()
                .username("username")
                .email("john@example.com")
                .password("pass")
                .build();

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(existingUser)))
                .andExpect(status().isConflict());
    }

    @Test
    @DisplayName("Create user endpoint call with invalid email unsuccessful")
    void createUser_withInvalidEmail_returns400StatusCode() throws Exception {
        CreateUserRequestDto existingUser = CreateUserRequestDto.builder()
                .username("username")
                .email("john_example")
                .password("pass")
                .build();

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(existingUser)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Create user endpoint call with blank email unsuccessful")
    void createUser_withBlankEmail_returns400StatusCode() throws Exception {
        CreateUserRequestDto existingUser = CreateUserRequestDto.builder()
                .username("username")
                .email("")
                .password("pass")
                .build();

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(existingUser)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Create user endpoint call with blank username unsuccessful")
    void createUser_withBlankUsername_returns400StatusCode() throws Exception {
        CreateUserRequestDto existingUser = CreateUserRequestDto.builder()
                .username(" ")
                .email("john@example.com")
                .password("pass")
                .build();

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(existingUser)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Create user endpoint call with blank password unsuccessful")
    void createUser_withBlankPassword_returns400StatusCode() throws Exception {
        CreateUserRequestDto existingUser = CreateUserRequestDto.builder()
                .username("john_doe")
                .email("john@example.com")
                .password(" ")
                .build();

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(existingUser)))
                .andExpect(status().isBadRequest());
    }

    private CreateUserRequestDto createUserRequestDto() {
        return CreateUserRequestDto.builder()
                .username("john_doe")
                .email("john@example.com")
                .password("password")
                .build();
    }
}
