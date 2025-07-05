package com.phoenixcode.Expense.Tracker.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ExpenseResponseDto {

    private UUID id;
    private BigDecimal amount;
    private String description;
    private String category;
    private LocalDate date;
}
