package com.phoenixcode.Expense.Tracker.service;


import com.phoenixcode.Expense.Tracker.dto.CreateUserRequestDto;
import com.phoenixcode.Expense.Tracker.dto.UserResponseDto;
import com.phoenixcode.Expense.Tracker.entity.User;

import java.util.UUID;

public interface UserService {
    UserResponseDto createUser(CreateUserRequestDto createUserRequestDto);

    UserResponseDto getUser(UUID id);

    UserResponseDto updateUser(UUID id, CreateUserRequestDto userRequestDto);

    void deleteUser(UUID id);
}
