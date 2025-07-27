package com.phoenixcode.Expense.Tracker.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Schema(description = "Create category request model")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class CreateCategoryRequestDto {

    @Schema(description = "Category name")
    @NotBlank(message = "Name must not be blank")
    private String name;

    @Schema(description = "Category description")
    private String description;

}
