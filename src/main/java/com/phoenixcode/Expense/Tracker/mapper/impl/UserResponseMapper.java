package com.phoenixcode.Expense.Tracker.mapper.impl;

import com.phoenixcode.Expense.Tracker.dto.UserResponseDto;
import com.phoenixcode.Expense.Tracker.entity.User;
import com.phoenixcode.Expense.Tracker.mapper.Mapper;
import org.modelmapper.ModelMapper;

public class UserResponseMapper implements Mapper<User, UserResponseDto> {

    private final ModelMapper modelMapper;

    public UserResponseMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public User mapTo(UserResponseDto userResponseDto) {
        return modelMapper.map(userResponseDto, User.class);
    }

    @Override
    public UserResponseDto mapFrom(User user) {
        return modelMapper.map(user, UserResponseDto.class);
    }
}