package com.phoenixcode.Expense.Tracker.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Schema(description = "Create or update expense request model")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class CreateExpenseRequestDto {

    @Schema(description = "Expense amount")
    @NotNull(message = "Amount is required")
    private BigDecimal amount;

    @Schema(description = "Expense description")
    @NotBlank(message = "Description must not be empty")
    private String description;

    @Schema(description = "Expense date")
    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

    @Schema(description = "Expense category ID")
    @NotNull(message = "Category is required")
    private UUID category;

    @Schema(description = "Expense user ID")
    @NotNull(message = "User id is required")
    private UUID user;
}
