package com.phoenixcode.Expense.Tracker.controller;

import com.phoenixcode.Expense.Tracker.dto.CreateExpenseRequestDto;
import com.phoenixcode.Expense.Tracker.dto.ExpenseResponseDto;
import com.phoenixcode.Expense.Tracker.entity.UserPrincipal;
import com.phoenixcode.Expense.Tracker.service.ExpenseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    public ResponseEntity<ExpenseResponseDto> createExpense(@AuthenticationPrincipal UserPrincipal userDetails,
            @Valid @RequestBody CreateExpenseRequestDto requestDto) {

        ExpenseResponseDto responseDto = expenseService.createExpense(requestDto, userDetails.getId());
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    @Operation(summary = "Get all Expenses of a user")
    @ApiResponse(responseCode = "200", description = "List of user expenses")
    @GetMapping
    public ResponseEntity<List<ExpenseResponseDto>> getExpenses(@AuthenticationPrincipal UserPrincipal userDetails) {

        List<ExpenseResponseDto> expenseResponseDtos = expenseService.getExpenses(userDetails.getId());
        return new ResponseEntity<>(expenseResponseDtos, HttpStatus.OK);
    }

    @Operation(summary = "Get a specific expense of a user")
    @ApiResponse(responseCode = "200", description = "Expense found")
    @GetMapping("/{id}")
    public ResponseEntity<ExpenseResponseDto> getExpense( @PathVariable UUID id,
                                                          @AuthenticationPrincipal UserPrincipal userDetails) {

        ExpenseResponseDto expenseResponseDto = expenseService.getExpense(id, userDetails.getId());
        return new ResponseEntity<>(expenseResponseDto, HttpStatus.OK);
    }

    @Operation(summary = "Update a specific expense of a user")
    @ApiResponse(responseCode = "200", description = "Expense updated successfully")
    @PutMapping("/{id}")
    public ResponseEntity<ExpenseResponseDto> updateExpense(@PathVariable UUID id,
                                                            @AuthenticationPrincipal UserPrincipal userDetails,
                                                            @RequestBody CreateExpenseRequestDto requestDto) {
        ExpenseResponseDto responseDto = expenseService.updateExpense(id, requestDto, userDetails.getId());
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @Operation(summary = "Delete a specific expense of a user")
    @ApiResponse(responseCode = "204", description = "Expense deleted")
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteExpense(@PathVariable UUID id,
                                                    @AuthenticationPrincipal UserPrincipal userDetails) {

        expenseService.deleteExpense(id, userDetails.getId());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
