package com.phoenixcode.Expense.Tracker.dto;

import lombok.*;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class CategoryResponseDto {

    private UUID id;
    private String name;
    private String description;
}
