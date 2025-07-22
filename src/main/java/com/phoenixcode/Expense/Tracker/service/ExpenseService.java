package com.phoenixcode.Expense.Tracker.service;

import com.phoenixcode.Expense.Tracker.dto.CreateExpenseRequestDto;
import com.phoenixcode.Expense.Tracker.dto.ExpenseResponseDto;
import jakarta.validation.Valid;

import java.util.List;
import java.util.UUID;

public interface ExpenseService {
    ExpenseResponseDto createExpense(@Valid CreateExpenseRequestDto requestDto);

    List<ExpenseResponseDto> getExpenses(UUID userId);

    ExpenseResponseDto getExpense(UUID id, UUID userId);

    ExpenseResponseDto updateExpense(UUID id, CreateExpenseRequestDto requestDto);

    void deleteExpense(UUID id, UUID userId);
}
