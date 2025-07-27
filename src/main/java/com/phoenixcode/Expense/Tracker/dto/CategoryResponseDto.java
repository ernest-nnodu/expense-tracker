package com.phoenixcode.Expense.Tracker.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.UUID;

@Schema(description = "Category response model")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class CategoryResponseDto {

    @Schema(description = "Category ID")
    private UUID id;

    @Schema(description = "Category name")
    private String name;

    @Schema(description = "Category description")
    private String description;
}
