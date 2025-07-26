package com.phoenixcode.Expense.Tracker.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "Create or update user request model")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class CreateUserRequestDto {

    @Schema(description = "User's username")
    @NotBlank(message = "username must not be empty")
    private String username;

    @Schema(description = "User's email address")
    @Email(message = "Email must be valid")
    @NotBlank(message = "Email must not be empty")
    private String email;

    @Schema(description = "User's password")
    @NotBlank(message = "Password must not be empty")
    private String password;
}
