package com.phoenixcode.Expense.Tracker.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.phoenixcode.Expense.Tracker.dto.*;
import com.phoenixcode.Expense.Tracker.repository.CategoryRepository;
import com.phoenixcode.Expense.Tracker.repository.ExpenseRepository;
import com.phoenixcode.Expense.Tracker.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;
import java.util.UUID;

import static com.phoenixcode.Expense.Tracker.util.TestDataUtil.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ExpenseControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void init() {
        expenseRepository.deleteAll();
        categoryRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("Create expense endpoint call with valid credentials successful")
    void createExpense_withValidCredentials_returnsExpenseAnd201Status() throws Exception {

        UserResponseDto userResponseDto = objectMapper.readValue(saveUser().getResponse()
                .getContentAsString(), UserResponseDto.class);

        CategoryResponseDto categoryResponseDto = objectMapper.readValue(saveCategory().getResponse()
                .getContentAsString(), CategoryResponseDto.class);

        CreateExpenseRequestDto expenseRequestDto = createExpenseRequestDto(
                userResponseDto.getId(), categoryResponseDto.getId());

        mockMvc.perform(post("/api/expenses")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(expenseRequestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.amount").value(199.87))
                .andExpect(jsonPath("$.description").value("Coffee"))
                .andExpect(jsonPath("$.category").value(categoryResponseDto.getName()));
    }

    @Test
    @DisplayName("Create expense endpoint call with invalid user is unsuccessful")
    void createExpense_withInvalidUser_returns404Status() throws Exception {
        UUID userId = UUID.randomUUID();

        CategoryResponseDto categoryResponseDto = objectMapper.readValue(saveCategory().getResponse()
                .getContentAsString(), CategoryResponseDto.class);

        CreateExpenseRequestDto expenseRequestDto = createExpenseRequestDto(userId,
                categoryResponseDto.getId());

        mockMvc.perform(post("/api/expenses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(expenseRequestDto)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Create expense endpoint call with invalid category is unsuccessful")
    void createExpense_withInvalidCategory_returns404Status() throws Exception {
        UserResponseDto userResponseDto = objectMapper.readValue(saveUser().getResponse()
                .getContentAsString(), UserResponseDto.class);

        UUID categoryId = UUID.randomUUID();

        CreateExpenseRequestDto expenseRequestDto = createExpenseRequestDto(userResponseDto.getId(),
                categoryId);

        mockMvc.perform(post("/api/expenses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(expenseRequestDto)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Create expense endpoint call without amount specified is unsuccessful")
    void createExpense_withoutAmount_returns400Status() throws Exception {

        UserResponseDto userResponseDto = objectMapper.readValue(saveUser().getResponse()
                .getContentAsString(), UserResponseDto.class);

        CategoryResponseDto categoryResponseDto = objectMapper.readValue(saveCategory().getResponse()
                .getContentAsString(), CategoryResponseDto.class);

        CreateExpenseRequestDto expenseRequestDto = createExpenseRequestDto(
                userResponseDto.getId(), categoryResponseDto.getId());
        expenseRequestDto.setAmount(null);

        mockMvc.perform(post("/api/expenses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(expenseRequestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Amount is required"));
    }

    @Test
    @DisplayName("Create expense endpoint call with blank description is unsuccessful")
    void createExpense_withoutBlankDescription_returns400Status() throws Exception {

        UserResponseDto userResponseDto = objectMapper.readValue(saveUser().getResponse()
                .getContentAsString(), UserResponseDto.class);

        CategoryResponseDto categoryResponseDto = objectMapper.readValue(saveCategory().getResponse()
                .getContentAsString(), CategoryResponseDto.class);

        CreateExpenseRequestDto expenseRequestDto = createExpenseRequestDto(
                userResponseDto.getId(), categoryResponseDto.getId());
        expenseRequestDto.setDescription("");

        mockMvc.perform(post("/api/expenses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(expenseRequestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Description must not be empty"));
    }

    @Test
    @DisplayName("Get expenses endpoint call with valid user id successful")
    void getExpenses_withValidUserId_returnsExpensesAnd200Status() throws Exception {
        UserResponseDto userResponseDto = objectMapper.readValue(saveUser().getResponse()
                .getContentAsString(), UserResponseDto.class);

        CategoryResponseDto categoryResponseDto = objectMapper.readValue(saveCategory().getResponse()
                .getContentAsString(), CategoryResponseDto.class);

        ExpenseResponseDto expenseResponseDto = objectMapper.readValue(
                saveExpense(userResponseDto.getId(), categoryResponseDto.getId())
                .getResponse()
                .getContentAsString(), ExpenseResponseDto.class);

        mockMvc.perform(get("/api/expenses?userId=" + userResponseDto.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(expenseResponseDto.getId().toString()))
                .andExpect(jsonPath("$[0].amount").value(expenseResponseDto.getAmount()));
    }

    @Test
    @DisplayName("Get expenses endpoint with invalid user id is unsuccessful")
    void getExpenses_withInvalidUserId_returns404Status() throws Exception {

        mockMvc.perform(get("/api/expenses?userId=" + UUID.randomUUID())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Get expense endpoint call with valid id and valid user id successful")
    void getExpense_withValidIdAndValidUserId_returnsExpenseAnd200Status() throws Exception {
        UserResponseDto userResponseDto = objectMapper.readValue(saveUser().getResponse()
                .getContentAsString(), UserResponseDto.class);

        CategoryResponseDto categoryResponseDto = objectMapper.readValue(saveCategory().getResponse()
                .getContentAsString(), CategoryResponseDto.class);

        ExpenseResponseDto expenseResponseDto = objectMapper.readValue(
                saveExpense(userResponseDto.getId(), categoryResponseDto.getId())
                        .getResponse()
                        .getContentAsString(), ExpenseResponseDto.class);

        mockMvc.perform(get("/api/expenses/" + expenseResponseDto.getId() +
                        "?userId=" + userResponseDto.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(expenseResponseDto.getId().toString()))
                .andExpect(jsonPath("$.amount").value(expenseResponseDto.getAmount()));
    }

    @Test
    @DisplayName("Get expense endpoint with invalid id is unsuccessful")
    void getExpense_withInvalidId_returns404Status() throws Exception {

        UserResponseDto userResponseDto = objectMapper.readValue(saveUser().getResponse()
                .getContentAsString(), UserResponseDto.class);

        mockMvc.perform(get("/api/expenses/" + UUID.randomUUID() + "?userId=" +
                        userResponseDto.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Update expense endpoint call with valid credentials successful")
    void updateExpense_withValidCredentials_returnsUpdatedExpenseAnd200Status() throws Exception {
        UserResponseDto userResponseDto = objectMapper.readValue(saveUser().getResponse()
                .getContentAsString(), UserResponseDto.class);

        CategoryResponseDto categoryResponseDto = objectMapper.readValue(saveCategory().getResponse()
                .getContentAsString(), CategoryResponseDto.class);

        ExpenseResponseDto expenseResponseDto = objectMapper.readValue(
                saveExpense(userResponseDto.getId(), categoryResponseDto.getId())
                        .getResponse()
                        .getContentAsString(), ExpenseResponseDto.class);

        CreateExpenseRequestDto updateExpenseDto = CreateExpenseRequestDto.builder()
                .category(categoryResponseDto.getId())
                .user(userResponseDto.getId())
                .description("Updated description")
                .amount(BigDecimal.valueOf(3400))
                .build();

        mockMvc.perform(put("/api/expenses/" + expenseResponseDto.getId().toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateExpenseDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(expenseResponseDto.getId().toString()))
                .andExpect(jsonPath("$.amount").value(updateExpenseDto.getAmount()))
                .andExpect(jsonPath("$.description").value(updateExpenseDto.getDescription()));
    }

    @Test
    @DisplayName("Delete expense endpoint call with valid credentials successful")
    void deleteExpense_withValidCredentials_returns204Status() throws Exception {
        UserResponseDto userResponseDto = objectMapper.readValue(saveUser().getResponse()
                .getContentAsString(), UserResponseDto.class);

        CategoryResponseDto categoryResponseDto = objectMapper.readValue(saveCategory().getResponse()
                .getContentAsString(), CategoryResponseDto.class);

        ExpenseResponseDto expenseResponseDto = objectMapper.readValue(
                saveExpense(userResponseDto.getId(), categoryResponseDto.getId())
                        .getResponse()
                        .getContentAsString(), ExpenseResponseDto.class);

        mockMvc.perform(delete("/api/expenses/" + expenseResponseDto.getId().toString()
                + "?userId=" + userResponseDto.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    private MvcResult saveExpense(UUID user, UUID category) throws Exception {

        CreateExpenseRequestDto expenseRequestDto = createExpenseRequestDto(user, category);

        return mockMvc.perform(post("/api/expenses")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(expenseRequestDto)))
                .andReturn();
    }

    private MvcResult saveUser() throws Exception {
        CreateUserRequestDto userRequestDto = createUserRequestDto();

        return mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userRequestDto)))
                .andReturn();
    }

    private MvcResult saveCategory() throws Exception {
        CreateCategoryRequestDto requestDto = createCategoryRequestDto();

        return mockMvc.perform(post("/api/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andReturn();
    }
}
