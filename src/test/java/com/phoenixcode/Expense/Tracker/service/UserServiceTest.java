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

import static com.phoenixcode.Expense.Tracker.util.TestDataUtil.*;
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
    void getUser_withInvalidId_throwsResourceNotFoundException() {
        when(userRepository.findById(mockUser.getId())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.getUser(mockUser.getId()));

        verify(userRepository).findById(mockUser.getId());
    }

    @Test
    @DisplayName("User with valid credentials updated successfully")
    void updateUser_withValidCredentials_returnsUser() {
        String username = "username";
        String email = "username@email.com";
        String password = "pass";
        CreateUserRequestDto userRequestDto = createUserDto(username, email, password);
        User updatedUser = User.builder()
                .id(mockUser.getId())
                .username(username)
                .email(email)
                .password(password)
                .build();
        UserResponseDto userResponseDto = createUserResponseDto(updatedUser);

        when(modelMapper.map(updatedUser, UserResponseDto.class)).thenReturn(userResponseDto);
        when(userRepository.findById(any())).thenReturn(Optional.of(mockUser));
        when(userRepository.existsByUsername(any())).thenReturn(false);
        when(userRepository.existsByEmail(any())).thenReturn(false);
        when(userRepository.save(any())).thenReturn(updatedUser);

        UserResponseDto returnedUser = userService.updateUser(mockUser.getId(), userRequestDto);

        assertAll(
                () -> assertEquals(updatedUser.getUsername(), returnedUser.getUsername()),
                () -> assertEquals(updatedUser.getEmail(), returnedUser.getEmail())
        );

        verify(userRepository).existsByEmail(any());
        verify(userRepository).existsByUsername(any());
        verify(userRepository, times(1)).save(updatedUser);
    }

    @Test
    @DisplayName("Delete user with valid id successful")
    void deleteUser_withValidId_deletesUser() {
        when(userRepository.findById(any())).thenReturn(Optional.ofNullable(mockUser));

        userService.deleteUser(mockUser.getId());

        verify(userRepository).delete(mockUser);
    }
}
