package com.phoenixcode.Expense.Tracker.controller;

import com.phoenixcode.Expense.Tracker.dto.CreateExpenseRequestDto;
import com.phoenixcode.Expense.Tracker.dto.ExpenseResponseDto;
import com.phoenixcode.Expense.Tracker.service.ExpenseService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/expenses")
public class ExpenseController {

    private final ExpenseService expenseService;

    public ExpenseController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    @PostMapping
    public ResponseEntity<ExpenseResponseDto> createExpense(
            @Valid @RequestBody CreateExpenseRequestDto requestDto) {

        ExpenseResponseDto responseDto = expenseService.createExpense(requestDto);
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<ExpenseResponseDto>> getExpenses(@RequestParam UUID userId) {

        List<ExpenseResponseDto> expenseResponseDtos = expenseService.getExpenses(userId);
        return new ResponseEntity<>(expenseResponseDtos, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExpenseResponseDto> getExpense(
            @PathVariable UUID id, @RequestParam UUID userId) {

        ExpenseResponseDto expenseResponseDto = expenseService.getExpense(id, userId);
        return new ResponseEntity<>(expenseResponseDto, HttpStatus.OK);
    }
}
