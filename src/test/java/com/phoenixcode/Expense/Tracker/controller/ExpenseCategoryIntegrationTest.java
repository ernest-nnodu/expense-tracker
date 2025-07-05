package com.phoenixcode.Expense.Tracker.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.phoenixcode.Expense.Tracker.repository.CategoryRepository;
import com.phoenixcode.Expense.Tracker.repository.ExpenseRepository;
import com.phoenixcode.Expense.Tracker.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class ExpenseCategoryIntegrationTest {

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
}
