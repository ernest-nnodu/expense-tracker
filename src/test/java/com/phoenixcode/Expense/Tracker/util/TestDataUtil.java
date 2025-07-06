package com.phoenixcode.Expense.Tracker.util;

import com.phoenixcode.Expense.Tracker.dto.*;
import com.phoenixcode.Expense.Tracker.entity.Category;
import com.phoenixcode.Expense.Tracker.entity.Expense;
import com.phoenixcode.Expense.Tracker.entity.User;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

public class TestDataUtil {

    private TestDataUtil() {

    }

    public static CreateUserRequestDto createUserRequestDto() {
        return CreateUserRequestDto.builder()
                .username("john_doe")
                .email("john@example.com")
                .password("password")
                .build();
    }

    public static User createMockUser() {
        return User.builder()
                .id(UUID.randomUUID())
                .username("user")
                .email("user@email.com")
                .password("password")
                .build();
    }

    public static CreateUserRequestDto createUserDto(String username, String email, String password) {
        return CreateUserRequestDto.builder()
                .username(username)
                .email(email)
                .password(password)
                .build();
    }

    public static UserResponseDto createUserResponseDto(User user) {
        return UserResponseDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .build();
    }

    public static CreateCategoryRequestDto createCategoryRequestDto() {
        return CreateCategoryRequestDto.builder()
                .name("Shopping")
                .description("Shopping description")
                .build();
    }

    public static Category createMockCategory() {
        return Category.builder()
                .id(UUID.randomUUID())
                .name("category")
                .description("description")
                .build();
    }

    public static CreateCategoryRequestDto createCategoryDto(String name, String description) {
        return CreateCategoryRequestDto.builder()
                .name(name)
                .description(description)
                .build();
    }

    public static CategoryResponseDto createCategoryResponseDto(Category category) {
        return CategoryResponseDto.builder()
                .id(category.getId())
                .name(category.getName())
                .description(category.getDescription())
                .build();
    }


    public static Expense createMockExpense() {

        return Expense.builder()
                .id(UUID.randomUUID())
                .amount(BigDecimal.valueOf(156))
                .description("Expense")
                .date(LocalDate.now())
                .category(createMockCategory())
                .user(createMockUser())
                .build();
    }

    public static CreateExpenseRequestDto createExpenseRequestDto(UUID user, UUID category) {
        return CreateExpenseRequestDto.builder()
                .amount(BigDecimal.valueOf(199.87))
                .description("Coffee")
                .date(LocalDate.now())
                .user(user)
                .category(category)
                .build();
    }

    public static CreateExpenseRequestDto createExpenseDto(BigDecimal amount, String description, LocalDate date,
                                                     UUID category, UUID user) {
        return CreateExpenseRequestDto.builder()
                .amount(amount)
                .description(description)
                .date(date)
                .category(category)
                .user(user)
                .build();
    }

    public static ExpenseResponseDto createExpenseResponseDto(Expense expense) {
        return ExpenseResponseDto.builder()
                .id(expense.getId())
                .amount(expense.getAmount())
                .description(expense.getDescription())
                .category(expense.getCategory().getName())
                .date(expense.getDate())
                .build();
    }
}
