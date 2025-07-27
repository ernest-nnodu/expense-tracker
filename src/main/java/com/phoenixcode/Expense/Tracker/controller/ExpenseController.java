package com.phoenixcode.Expense.Tracker.controller;

import com.phoenixcode.Expense.Tracker.dto.CreateExpenseRequestDto;
import com.phoenixcode.Expense.Tracker.dto.ExpenseResponseDto;
import com.phoenixcode.Expense.Tracker.service.ExpenseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(name = "Expenses", description = "Provides CRUD operations for managing expenses")
@RestController
@RequestMapping("/api/expenses")
public class ExpenseController {

    private final ExpenseService expenseService;

    public ExpenseController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    @Operation(summary = "Create Expense")
    @ApiResponse(responseCode = "201", description = "Expense created successfully")
    @PostMapping
    public ResponseEntity<ExpenseResponseDto> createExpense(
            @Valid @RequestBody CreateExpenseRequestDto requestDto) {

        ExpenseResponseDto responseDto = expenseService.createExpense(requestDto);
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    @Operation(summary = "Get all Expenses of a user")
    @ApiResponse(responseCode = "200", description = "List of user expenses")
    @GetMapping
    public ResponseEntity<List<ExpenseResponseDto>> getExpenses(@RequestParam UUID userId) {

        List<ExpenseResponseDto> expenseResponseDtos = expenseService.getExpenses(userId);
        return new ResponseEntity<>(expenseResponseDtos, HttpStatus.OK);
    }

    @Operation(summary = "Get a specific expense of a user")
    @ApiResponse(responseCode = "200", description = "Expense found")
    @GetMapping("/{id}")
    public ResponseEntity<ExpenseResponseDto> getExpense(
            @PathVariable UUID id, @RequestParam UUID userId) {

        ExpenseResponseDto expenseResponseDto = expenseService.getExpense(id, userId);
        return new ResponseEntity<>(expenseResponseDto, HttpStatus.OK);
    }

    @Operation(summary = "Update a specific expense of a user")
    @ApiResponse(responseCode = "200", description = "Expense updated successfully")
    @PutMapping("/{id}")
    public ResponseEntity<ExpenseResponseDto> updateExpense(@PathVariable UUID id,
                                                            @RequestBody CreateExpenseRequestDto requestDto) {
        ExpenseResponseDto responseDto = expenseService.updateExpense(id, requestDto);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @Operation(summary = "Delete a specific expense of a user")
    @ApiResponse(responseCode = "204", description = "Expense deleted")
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteExpense(@PathVariable UUID id, @RequestParam UUID userId) {

        expenseService.deleteExpense(id, userId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
