package com.phoenixcode.Expense.Tracker.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ExpenseResponseDto {

    private UUID id;
    private BigDecimal amount;
    private String description;
    private String category;
    private LocalDate date;
}
