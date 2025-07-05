package com.phoenixcode.Expense.Tracker.service;

import com.phoenixcode.Expense.Tracker.dto.CreateExpenseRequestDto;
import com.phoenixcode.Expense.Tracker.dto.ExpenseResponseDto;
import com.phoenixcode.Expense.Tracker.entity.Expense;
import com.phoenixcode.Expense.Tracker.exception.ResourceNotFoundException;
import com.phoenixcode.Expense.Tracker.repository.CategoryRepository;
import com.phoenixcode.Expense.Tracker.repository.ExpenseRepository;
import com.phoenixcode.Expense.Tracker.repository.UserRepository;
import com.phoenixcode.Expense.Tracker.service.impl.ExpenseServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.time.LocalDate;
import java.util.Optional;

import static com.phoenixcode.Expense.Tracker.util.TestDataUtil.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ExpenseServiceTest {

    @Mock
    private ExpenseRepository expenseRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private ExpenseServiceImpl expenseService;

    private Expense mockExpense;

    @BeforeEach
    void init() {

        mockExpense = createMockExpense();
    }

    @Test
    @DisplayName("Create expense with valid credentials successful")
    void createExpense_withValidCredentials_returnsExpense() {
        CreateExpenseRequestDto requestDto = createExpenseDto(mockExpense.getAmount(), mockExpense.getDescription(),
                LocalDate.now(), mockExpense.getCategory().getId(), mockExpense.getUser().getId());
        ExpenseResponseDto responseDto = createExpenseResponseDto(mockExpense);

        when(userRepository.findById(any())).thenReturn(Optional.ofNullable(mockExpense.getUser()));
        when(categoryRepository.findById(any())).thenReturn(Optional.ofNullable(mockExpense.getCategory()));
        when(modelMapper.map(requestDto, Expense.class)).thenReturn(mockExpense);
        when(modelMapper.map(mockExpense, ExpenseResponseDto.class)).thenReturn(responseDto);
        when(expenseRepository.save(any())).thenReturn(mockExpense);

        ExpenseResponseDto returnedExpense = expenseService.createExpense(requestDto);

        assertAll(
                () -> assertEquals(mockExpense.getId(), returnedExpense.getId()),
                () -> assertEquals(mockExpense.getDescription(), returnedExpense.getDescription()),
                () -> assertEquals(mockExpense.getAmount(), returnedExpense.getAmount()),
                () -> assertEquals(mockExpense.getCategory().getName(), returnedExpense.getCategory())
        );

        verify(expenseRepository).save(mockExpense);
    }

    @Test
    @DisplayName("Create expense with invalid user id unsuccessful")
    void createExpense_withInvalidUserId_throwsResourceNotFoundException() {
        CreateExpenseRequestDto requestDto = createExpenseDto(mockExpense.getAmount(), mockExpense.getDescription(),
                LocalDate.now(), mockExpense.getCategory().getId(), mockExpense.getUser().getId());
        ExpenseResponseDto responseDto = createExpenseResponseDto(mockExpense);

        when(userRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> expenseService.createExpense(requestDto));

        verify(userRepository).findById(requestDto.getUser());
    }

    @Test
    @DisplayName("Create expense with invalid category id unsuccessful")
    void createExpense_withInvalidCategoryId_throwsResourceNotFoundException() {
        CreateExpenseRequestDto requestDto = createExpenseDto(mockExpense.getAmount(), mockExpense.getDescription(),
                LocalDate.now(), mockExpense.getCategory().getId(), mockExpense.getUser().getId());
        ExpenseResponseDto responseDto = createExpenseResponseDto(mockExpense);

        when(userRepository.findById(any())).thenReturn(Optional.ofNullable(mockExpense.getUser()));
        when(categoryRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> expenseService.createExpense(requestDto));

        verify(categoryRepository).findById(requestDto.getCategory());
    }
}
