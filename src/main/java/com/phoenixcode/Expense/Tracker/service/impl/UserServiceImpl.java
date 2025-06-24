package com.phoenixcode.Expense.Tracker.service.impl;

import com.phoenixcode.Expense.Tracker.dto.CreateUserRequestDto;
import com.phoenixcode.Expense.Tracker.entity.User;
import com.phoenixcode.Expense.Tracker.exception.UserAlreadyExistsException;
import com.phoenixcode.Expense.Tracker.repository.UserRepository;
import com.phoenixcode.Expense.Tracker.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User createUser(CreateUserRequestDto createUserRequestDto) {

        if (userRepository.existsByUsername(createUserRequestDto.getUsername())) {
            throw new UserAlreadyExistsException("User already exists with username " +
                    createUserRequestDto.getUsername());
        }

        if (userRepository.existsByEmail(createUserRequestDto.getEmail())) {
            throw new UserAlreadyExistsException("User already exists with email " +
                    createUserRequestDto.getEmail());
        }

        User user = User.builder()
                .username(createUserRequestDto.getUsername())
                .email(createUserRequestDto.getEmail())
                .password(createUserRequestDto.getPassword())
                .build();

        return userRepository.save(user);
    }
}
