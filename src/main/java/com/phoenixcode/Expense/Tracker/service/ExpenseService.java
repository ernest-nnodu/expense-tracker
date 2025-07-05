package com.phoenixcode.Expense.Tracker.service;

import com.phoenixcode.Expense.Tracker.dto.CreateExpenseRequestDto;
import com.phoenixcode.Expense.Tracker.dto.ExpenseResponseDto;
import jakarta.validation.Valid;

public interface ExpenseService {
    ExpenseResponseDto createExpense(@Valid CreateExpenseRequestDto requestDto);
}
