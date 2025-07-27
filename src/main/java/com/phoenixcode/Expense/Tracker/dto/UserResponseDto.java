package com.phoenixcode.Expense.Tracker.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Schema(description = "User response model")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class UserResponseDto {

    @Schema(description = "User's ID")
    private UUID id;

    @Schema(description = "User's username")
    private String username;

    @Schema(description = "User's email address")
    private String email;
}
