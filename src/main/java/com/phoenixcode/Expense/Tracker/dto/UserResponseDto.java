package com.phoenixcode.Expense.Tracker.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class UserResponseDto {

    private UUID id;
    private String username;
    private String email;
}
