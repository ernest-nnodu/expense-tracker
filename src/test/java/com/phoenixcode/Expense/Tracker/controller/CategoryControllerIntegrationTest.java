package com.phoenixcode.Expense.Tracker.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.phoenixcode.Expense.Tracker.dto.CategoryResponseDto;
import com.phoenixcode.Expense.Tracker.dto.CreateCategoryRequestDto;
import com.phoenixcode.Expense.Tracker.dto.CreateUserRequestDto;
import com.phoenixcode.Expense.Tracker.dto.UserResponseDto;
import com.phoenixcode.Expense.Tracker.repository.CategoryRepository;
import com.phoenixcode.Expense.Tracker.repository.UserRepository;
import com.phoenixcode.Expense.Tracker.service.impl.UserDetailsServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.UUID;

import static com.phoenixcode.Expense.Tracker.util.TestDataUtil.createCategoryRequestDto;
import static com.phoenixcode.Expense.Tracker.util.TestDataUtil.createUserRequestDto;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class CategoryControllerIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    UserDetailsServiceImpl userDetailsService;

    @Autowired
    private ObjectMapper objectMapper;

    private UserResponseDto userTestData;

    @BeforeEach
    void init() throws Exception {
        categoryRepository.deleteAll();
        initUserDatabase();
    }

    @Test
    @DisplayName("Create category endpoint call with valid category credentials successful")
    void createCategory_withValidCredentials_returnsCategoryAnd201StatusCode() throws Exception {
        CreateCategoryRequestDto requestDto = createCategoryRequestDto();

        mockMvc.perform(post("/api/categories")
                        .with(user(userDetailsService.loadUserByUsername(userTestData.getUsername())))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("Shopping"))
                .andExpect(jsonPath("$.description").value("Shopping description"));
    }

    @Test
    @DisplayName("Create category endpoint call with existing category name unsuccessful")
    void createCategory_withExistingCategoryName_returns409StatusCode() throws Exception {

        saveCategory();
        CreateCategoryRequestDto existingCategory = CreateCategoryRequestDto.builder()
                .name("Shopping")
                .description("description")
                .build();

        mockMvc.perform(post("/api/categories")
                        .with(user(userDetailsService.loadUserByUsername(userTestData.getUsername())))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(existingCategory)))
                .andExpect(status().isConflict());
    }

    @Test
    @DisplayName("Create category endpoint call with blank category name unsuccessful")
    void createCategory_withBlankCategoryName_returns400StatusCode() throws Exception {
        CreateCategoryRequestDto requestDto = CreateCategoryRequestDto.builder()
                .name("")
                .description("description")
                .build();

        mockMvc.perform(post("/api/categories")
                        .with(user(userDetailsService.loadUserByUsername(userTestData.getUsername())))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Get all categories returns 200 ok status")
    void getAllCategories_returns200StatusCode() throws Exception {

        mockMvc.perform(get("/api/categories")
                        .with(user(userDetailsService.loadUserByUsername(userTestData.getUsername())))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
    
    @Test
    @DisplayName("Get all categories returns list of categories")
    void getAllCategories_returnsListOfCategories() throws Exception {

        saveCategory();

        mockMvc.perform(get("/api/categories")
                        .with(user(userDetailsService.loadUserByUsername(userTestData.getUsername())))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("[0].id").exists())
                .andExpect(jsonPath("[0].name").value("Shopping"))
                .andExpect(jsonPath("[0].description").value("Shopping description"));
    }

    @Test
    @DisplayName("Get category with valid id returns category")
    void getCategory_withValidId_returnsCategoryAnd200StatusCode() throws Exception {

        MvcResult result = saveCategory();
        String responseBody = result.getResponse().getContentAsString();
        CategoryResponseDto responseDto = objectMapper.readValue(responseBody, CategoryResponseDto.class);

        mockMvc.perform(get("/api/categories/" + responseDto.getId())
                        .with(user(userDetailsService.loadUserByUsername(userTestData.getUsername())))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(responseDto.getId().toString()))
                .andExpect(jsonPath("$.name").value(responseDto.getName()));
    }

    @Test
    @DisplayName("Get category with invalid id returns 404 status code")
    void getCategory_withInvalidId_returns404StatusCode() throws Exception {

        mockMvc.perform(get("/api/categories/" + UUID.randomUUID())
                        .with(user(userDetailsService.loadUserByUsername(userTestData.getUsername())))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Delete category with valid id deletes category")
    void deleteCategory_withValidId_deletesCategory() throws Exception {

        MvcResult result = saveCategory();
        String responseBody = result.getResponse().getContentAsString();
        CategoryResponseDto responseDto = objectMapper.readValue(responseBody, CategoryResponseDto.class);

        mockMvc.perform(delete("/api/categories/" + responseDto.getId())
                        .with(user(userDetailsService.loadUserByUsername(userTestData.getUsername())))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/categories/" + responseDto.getId())
                        .with(user(userDetailsService.loadUserByUsername(userTestData.getUsername())))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Delete category with invalid id returns 404 status code")
    void deleteCategory_withInvalidId_returns404StatusCode() throws Exception {

        mockMvc.perform(delete("/api/categories/" + UUID.randomUUID())
                        .with(user(userDetailsService.loadUserByUsername(userTestData.getUsername())))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    private MvcResult saveCategory() throws Exception {
        CreateCategoryRequestDto requestDto = createCategoryRequestDto();
        return mockMvc.perform(post("/api/categories")
                        .with(user(userDetailsService.loadUserByUsername(userTestData.getUsername())))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andReturn();
    }

    private MvcResult saveUser() throws Exception {
        CreateUserRequestDto userRequestDto = createUserRequestDto();

        return mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequestDto)))
                .andReturn();
    }

    private void initUserDatabase() throws Exception {
        userRepository.deleteAll();
        userTestData = objectMapper.readValue(saveUser().getResponse()
                .getContentAsString(), UserResponseDto.class);
    }
}
