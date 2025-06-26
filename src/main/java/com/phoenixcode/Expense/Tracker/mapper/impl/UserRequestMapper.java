package com.phoenixcode.Expense.Tracker.mapper.impl;

import com.phoenixcode.Expense.Tracker.dto.CreateUserRequestDto;
import com.phoenixcode.Expense.Tracker.entity.User;
import com.phoenixcode.Expense.Tracker.mapper.Mapper;
import org.modelmapper.ModelMapper;

public class UserRequestMapper implements Mapper<User, CreateUserRequestDto> {

    private final ModelMapper modelMapper;

    public UserRequestMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public User mapTo(CreateUserRequestDto createUserRequestDto) {
        return modelMapper.map(createUserRequestDto, User.class);
    }

    @Override
    public CreateUserRequestDto mapFrom(User user) {
        return modelMapper.map(user, CreateUserRequestDto.class);
    }
}
