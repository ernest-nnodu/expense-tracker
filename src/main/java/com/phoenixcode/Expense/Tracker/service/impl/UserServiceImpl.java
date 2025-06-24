package com.phoenixcode.Expense.Tracker.service.impl;

import com.phoenixcode.Expense.Tracker.dto.CreateUserRequestDto;
import com.phoenixcode.Expense.Tracker.dto.UserResponseDto;
import com.phoenixcode.Expense.Tracker.entity.User;
import com.phoenixcode.Expense.Tracker.exception.UserAlreadyExistsException;
import com.phoenixcode.Expense.Tracker.repository.UserRepository;
import com.phoenixcode.Expense.Tracker.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public UserServiceImpl(UserRepository userRepository, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public UserResponseDto createUser(CreateUserRequestDto createUserRequestDto) {

        if (userRepository.existsByUsername(createUserRequestDto.getUsername())) {
            throw new UserAlreadyExistsException("User already exists with username " +
                    createUserRequestDto.getUsername());
        }

        if (userRepository.existsByEmail(createUserRequestDto.getEmail())) {
            throw new UserAlreadyExistsException("User already exists with email " +
                    createUserRequestDto.getEmail());
        }

        User user = modelMapper.map(createUserRequestDto, User.class);
        User savedUser = userRepository.save(user);
        return modelMapper.map(savedUser, UserResponseDto.class);
    }
}
