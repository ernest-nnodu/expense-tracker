package com.phoenixcode.Expense.Tracker.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Schema(description = "Expense response model")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ExpenseResponseDto {

    @Schema(description = "Expense ID")
    private UUID id;

    @Schema(description = "Expense amount")
    private BigDecimal amount;

    @Schema(description = "Expense description")
    private String description;

    @Schema(description = "Expense category")
    private String category;

    @Schema(description = "Expense date")
    private LocalDate date;
}
