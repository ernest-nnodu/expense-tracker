package com.phoenixcode.Expense.Tracker.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class CreateCategoryRequestDto {

    @NotBlank
    private String name;

    private String description;

}
