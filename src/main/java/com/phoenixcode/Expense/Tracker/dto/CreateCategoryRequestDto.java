package com.phoenixcode.Expense.Tracker.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class CreateCategoryRequestDto {

    @NotBlank(message = "Name must not be blank")
    private String name;

    private String description;

}
