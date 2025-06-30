package com.phoenixcode.Expense.Tracker.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
public class CreateExpenseRequestDto {

    @NotNull(message = "Amount is required")
    private BigDecimal amount;

    @NotBlank(message = "Description must not be empty")
    private String description;

    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

    @NotNull(message = "Category is required")
    private UUID categoryId;

    @NotNull(message = "User id is required")
    private UUID userId;
}
