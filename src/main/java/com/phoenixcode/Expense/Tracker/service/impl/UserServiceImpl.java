package com.phoenixcode.Expense.Tracker.service.impl;

import com.phoenixcode.Expense.Tracker.dto.CreateUserRequestDto;
import com.phoenixcode.Expense.Tracker.dto.UserResponseDto;
import com.phoenixcode.Expense.Tracker.entity.User;
import com.phoenixcode.Expense.Tracker.exception.ResourceAlreadyExistsException;
import com.phoenixcode.Expense.Tracker.exception.ResourceNotFoundException;
import com.phoenixcode.Expense.Tracker.repository.UserRepository;
import com.phoenixcode.Expense.Tracker.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.UUID;

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
            throw new ResourceAlreadyExistsException("User already exists with username " +
                    createUserRequestDto.getUsername());
        }

        if (userRepository.existsByEmail(createUserRequestDto.getEmail())) {
            throw new ResourceAlreadyExistsException("User already exists with email " +
                    createUserRequestDto.getEmail());
        }

        User user = modelMapper.map(createUserRequestDto, User.class);
        User savedUser = userRepository.save(user);
        return modelMapper.map(savedUser, UserResponseDto.class);
    }

    @Override
    public UserResponseDto getUser(UUID id) {
        User returnedUser = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + id));

        return modelMapper.map(returnedUser, UserResponseDto.class);
    }

    @Override
    public UserResponseDto updateUser(UUID id, CreateUserRequestDto userRequestDto) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + id));

        if (!(userRequestDto.getUsername().equals(existingUser.getUsername()))) {
           if(userRepository.existsByUsername(userRequestDto.getUsername())) {
               throw new ResourceAlreadyExistsException("User already exists with username "
                       + userRequestDto.getUsername());
           }
        }

        if (!(userRequestDto.getEmail().equals(existingUser.getEmail()))) {
            if(userRepository.existsByEmail(userRequestDto.getEmail())) {
                throw new ResourceAlreadyExistsException("User already exists with email "
                        + userRequestDto.getEmail());
            }
        }

        existingUser.setUsername(userRequestDto.getUsername());
        existingUser.setEmail(userRequestDto.getEmail());
        existingUser.setPassword(userRequestDto.getPassword());
        return modelMapper.map(userRepository.save(existingUser), UserResponseDto.class);
    }

    @Override
    public void deleteUser(UUID id) {
        User userToDelete = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + id));
        userRepository.delete(userToDelete);
    }
}
