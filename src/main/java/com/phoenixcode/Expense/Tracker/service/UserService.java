package com.phoenixcode.Expense.Tracker.service;


import com.phoenixcode.Expense.Tracker.dto.CreateUserRequestDto;
import com.phoenixcode.Expense.Tracker.entity.User;

public interface UserService {
    User createUser(CreateUserRequestDto createUserRequestDto);
}
