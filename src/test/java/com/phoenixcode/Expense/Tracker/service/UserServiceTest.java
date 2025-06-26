package com.phoenixcode.Expense.Tracker.service;

import com.phoenixcode.Expense.Tracker.dto.CreateUserRequestDto;
import com.phoenixcode.Expense.Tracker.dto.UserResponseDto;
import com.phoenixcode.Expense.Tracker.entity.User;
import com.phoenixcode.Expense.Tracker.exception.ResourceAlreadyExistsException;
import com.phoenixcode.Expense.Tracker.exception.ResourceNotFoundException;
import com.phoenixcode.Expense.Tracker.repository.UserRepository;
import com.phoenixcode.Expense.Tracker.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private UserServiceImpl userService;

    private User mockUser;

    @BeforeEach
    void init() {
        mockUser = createMockUser();
    }

    @Test
    @DisplayName("User with valid credentials created successfully")
    void createUser_withValidCredentials_returnsUser() {
        String username = "user";
        String email = "user@email.com";
        String password = "password";
        CreateUserRequestDto userRequestDto = createUserDto(username, email, password);
        UserResponseDto userResponseDto = createUserResponseDto(mockUser);

        when(modelMapper.map(userRequestDto, User.class)).thenReturn(mockUser);
        when(modelMapper.map(mockUser, UserResponseDto.class)).thenReturn(userResponseDto);
        when(userRepository.save(any())).thenReturn(mockUser);

        UserResponseDto returnedUser = userService.createUser(userRequestDto);

        assertAll(
                () -> assertEquals(mockUser.getUsername(), returnedUser.getUsername()),
                () -> assertEquals(mockUser.getEmail(), returnedUser.getEmail())
        );

        verify(userRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("User with existing username not created")
    void createUser_withExistingUsername_throwsResourceAlreadyExistsException() {
        String username = "user";
        String email = "user@email.com";
        String password = "password";
        CreateUserRequestDto userRequestDto = createUserDto(username, email, password);
        when(userRepository.existsByUsername(mockUser.getUsername())).thenReturn(true);

        assertThrows(ResourceAlreadyExistsException.class, () -> userService.createUser(userRequestDto));

        verify(userRepository, times(1)).existsByUsername(mockUser.getUsername());
    }

    @Test
    @DisplayName("User with existing email not created")
    void createUser_withExistingEmail_throwsResourceAlreadyExistsException() {
        String username = "user";
        String email = "user@email.com";
        String password = "password";
        CreateUserRequestDto userRequestDto = createUserDto(username, email, password);
        when(userRepository.existsByEmail(mockUser.getEmail())).thenReturn(true);

        assertThrows(ResourceAlreadyExistsException.class, () -> userService.createUser(userRequestDto));

        verify(userRepository, times(1)).existsByEmail(mockUser.getEmail());
    }

    @Test
    @DisplayName("Get user with valid id successful")
    void getUser_withValidId_returnsUser() {
        UserResponseDto mockUserResponseDto = createUserResponseDto(mockUser);
        when(userRepository.findById(mockUser.getId())).thenReturn(Optional.of(mockUser));
        when(modelMapper.map(mockUser, UserResponseDto.class)).thenReturn(mockUserResponseDto);

        UserResponseDto userResponseDto = userService.getUser(mockUser.getId());

        assertAll(
                () -> assertNotNull(userResponseDto),
                () -> assertEquals(mockUser.getId(), userResponseDto.getId()),
                () -> assertEquals(mockUser.getUsername(), userResponseDto.getUsername()),
                () -> assertEquals(mockUser.getEmail(), userResponseDto.getEmail())
        );

        verify(userRepository).findById(mockUser.getId());
    }

    @Test
    @DisplayName("Get user with invalid id unsuccessful")
    void getUser_withInvalidId_throwsUserNotFoundException() {
        when(userRepository.findById(mockUser.getId())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.getUser(mockUser.getId()));

        verify(userRepository).findById(mockUser.getId());
    }

    private User createMockUser() {
        return User.builder()
                .id(UUID.randomUUID())
                .username("user")
                .email("user@email.com")
                .password("password")
                .build();
    }

    private CreateUserRequestDto createUserDto(String username, String email, String password) {
        return CreateUserRequestDto.builder()
                .username(username)
                .email(email)
                .password(password)
                .build();
    }

    private UserResponseDto createUserResponseDto(User user) {
        return UserResponseDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .build();
    }
}
