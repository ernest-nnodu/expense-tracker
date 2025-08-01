package com.phoenixcode.Expense.Tracker.service;

import com.phoenixcode.Expense.Tracker.dto.CreateExpenseRequestDto;
import com.phoenixcode.Expense.Tracker.dto.ExpenseResponseDto;
import com.phoenixcode.Expense.Tracker.entity.Expense;
import com.phoenixcode.Expense.Tracker.exception.ResourceNotFoundException;
import com.phoenixcode.Expense.Tracker.repository.CategoryRepository;
import com.phoenixcode.Expense.Tracker.repository.ExpenseRepository;
import com.phoenixcode.Expense.Tracker.repository.UserRepository;
import com.phoenixcode.Expense.Tracker.service.impl.ExpenseServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static com.phoenixcode.Expense.Tracker.util.TestDataUtil.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ExpenseServiceTest {

    @Mock
    private ExpenseRepository expenseRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private ExpenseServiceImpl expenseService;

    private Expense mockExpense;

    @BeforeEach
    void init() {

        mockExpense = createMockExpense();
    }

    @Test
    @DisplayName("Create expense with valid credentials successful")
    void createExpense_withValidCredentials_returnsExpense() {
        CreateExpenseRequestDto requestDto = createExpenseDto(mockExpense.getAmount(), mockExpense.getDescription(),
                LocalDate.now(), mockExpense.getCategory().getId());
        ExpenseResponseDto responseDto = createExpenseResponseDto(mockExpense);

        when(userRepository.findById(any())).thenReturn(Optional.ofNullable(mockExpense.getUser()));
        when(categoryRepository.findById(any())).thenReturn(Optional.ofNullable(mockExpense.getCategory()));
        when(modelMapper.map(requestDto, Expense.class)).thenReturn(mockExpense);
        when(modelMapper.map(mockExpense, ExpenseResponseDto.class)).thenReturn(responseDto);
        when(expenseRepository.save(any())).thenReturn(mockExpense);

        ExpenseResponseDto returnedExpense = expenseService.createExpense(requestDto, mockExpense.getUser().getId());

        assertAll(
                () -> assertEquals(mockExpense.getId(), returnedExpense.getId()),
                () -> assertEquals(mockExpense.getDescription(), returnedExpense.getDescription()),
                () -> assertEquals(mockExpense.getAmount(), returnedExpense.getAmount()),
                () -> assertEquals(mockExpense.getCategory().getName(), returnedExpense.getCategory())
        );

        verify(expenseRepository).save(mockExpense);
    }

    @Test
    @DisplayName("Create expense with invalid user id unsuccessful")
    void createExpense_withInvalidUserId_throwsResourceNotFoundException() {
        CreateExpenseRequestDto requestDto = createExpenseDto(mockExpense.getAmount(), mockExpense.getDescription(),
                LocalDate.now(), mockExpense.getCategory().getId());
        ExpenseResponseDto responseDto = createExpenseResponseDto(mockExpense);

        when(userRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> expenseService.createExpense(requestDto,
                mockExpense.getUser().getId()));

        verify(userRepository).findById(mockExpense.getUser().getId());
    }

    @Test
    @DisplayName("Create expense with invalid category id unsuccessful")
    void createExpense_withInvalidCategoryId_throwsResourceNotFoundException() {
        CreateExpenseRequestDto requestDto = createExpenseDto(mockExpense.getAmount(), mockExpense.getDescription(),
                LocalDate.now(), mockExpense.getCategory().getId());
        ExpenseResponseDto responseDto = createExpenseResponseDto(mockExpense);

        when(userRepository.findById(any())).thenReturn(Optional.ofNullable(mockExpense.getUser()));
        when(categoryRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> expenseService.createExpense(requestDto,
                mockExpense.getUser().getId()));

        verify(categoryRepository).findById(requestDto.getCategory());
    }

    @Test
    @DisplayName("Get expenses with valid user id is successful")
    void getExpenses_withValidUserId_returnsListOfExpenses() {
        ExpenseResponseDto responseDto = createExpenseResponseDto(mockExpense);

        when(userRepository.findById(any())).thenReturn(Optional.ofNullable(mockExpense.getUser()));
        when(modelMapper.map(mockExpense, ExpenseResponseDto.class)).thenReturn(responseDto);
        when(expenseRepository.findAllByUser(any())).thenReturn((List.of(mockExpense)));

        List<ExpenseResponseDto> expenseResponseDtos = expenseService.getExpenses(mockExpense.getUser().getId());

        assertAll(
                () -> assertFalse(expenseResponseDtos.isEmpty()),
                () -> assertEquals(mockExpense.getId(), expenseResponseDtos.getFirst().getId()),
                () -> assertEquals(mockExpense.getAmount(), expenseResponseDtos.getFirst().getAmount()),
                () -> assertEquals(mockExpense.getCategory().getName(), expenseResponseDtos.getFirst().getCategory())
        );

        verify(expenseRepository).findAllByUser(mockExpense.getUser());
    }

    @Test
    @DisplayName("Get expenses with invalid user id is unsuccessful")
    void getExpenses_withInvalidUserId_throwsResourceNotFoundException() {
        ExpenseResponseDto responseDto = createExpenseResponseDto(mockExpense);

        when(userRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> expenseService.getExpenses(
                mockExpense.getUser().getId()));

        verify(userRepository).findById(mockExpense.getUser().getId());
    }

    @Test
    @DisplayName("Get expense with valid id and user id is successful")
    void getExpense_withValidIdAndUserId_returnsExpense() {
        ExpenseResponseDto responseDto = createExpenseResponseDto(mockExpense);

        when(userRepository.findById(any())).thenReturn(Optional.ofNullable(mockExpense.getUser()));
        when(modelMapper.map(mockExpense, ExpenseResponseDto.class)).thenReturn(responseDto);
        when(expenseRepository.findByIdAndUser(any(), any())).thenReturn(Optional.ofNullable(mockExpense));

        ExpenseResponseDto expenseResponseDto = expenseService.getExpense(mockExpense.getId(),
                mockExpense.getUser().getId());

        assertAll(
                () -> assertEquals(mockExpense.getId(), expenseResponseDto.getId()),
                () -> assertEquals(mockExpense.getAmount(), expenseResponseDto.getAmount()),
                () -> assertEquals(mockExpense.getDescription(), expenseResponseDto.getDescription())
        );

        verify(expenseRepository).findByIdAndUser(mockExpense.getId(), mockExpense.getUser());
    }

    @Test
    @DisplayName("Get expense with invalid user id is unsuccessful")
    void getExpense_withValidIdAndInvalidUserId_throwsResourceNotFoundException() {
        ExpenseResponseDto responseDto = createExpenseResponseDto(mockExpense);

        when(userRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> expenseService.getExpense(mockExpense.getId(),
                mockExpense.getUser().getId()));

        verify(userRepository).findById(mockExpense.getUser().getId());
    }

    @Test
    @DisplayName("Get expense with invalid id is unsuccessful")
    void getExpense_withInvalidIdAndValidUserId_throwsResourceNotFoundException() {
        ExpenseResponseDto responseDto = createExpenseResponseDto(mockExpense);

        when(userRepository.findById(any())).thenReturn(Optional.ofNullable(mockExpense.getUser()));
        when(expenseRepository.findByIdAndUser(any(), any())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> expenseService.getExpense(mockExpense.getId(),
                mockExpense.getUser().getId()));

        verify(userRepository).findById(mockExpense.getUser().getId());
        verify(expenseRepository).findByIdAndUser(mockExpense.getId(), mockExpense.getUser());
    }

    @Test
    @DisplayName("Update expense with valid id is successful")
    void updateExpense_withValidId_returnsUpdatedExpense() {

        CreateExpenseRequestDto requestDto = createExpenseDto(BigDecimal.valueOf(3400), "Updated description",
                LocalDate.now(), mockExpense.getCategory().getId());
        mockExpense.setDescription("Updated description");
        mockExpense.setAmount(BigDecimal.valueOf(3400));

        when(userRepository.findById(any())).thenReturn(Optional.ofNullable(mockExpense.getUser()));
        when(categoryRepository.findById(any())).thenReturn(Optional.ofNullable(mockExpense.getCategory()));
        when(expenseRepository.findById(any())).thenReturn(Optional.ofNullable(mockExpense));
        when(modelMapper.map(mockExpense, ExpenseResponseDto.class)).thenReturn(
                createExpenseResponseDto(mockExpense));
        when(expenseRepository.save(any())).thenReturn(mockExpense);

        ExpenseResponseDto updatedExpenseDto = expenseService.updateExpense(mockExpense.getId(), requestDto,
                mockExpense.getUser().getId());

        assertAll(
                () -> assertEquals(requestDto.getDescription(), updatedExpenseDto.getDescription()),
                () -> assertEquals(requestDto.getAmount(), updatedExpenseDto.getAmount())
        );

        verify(expenseRepository).save(mockExpense);
    }

    @Test
    @DisplayName("Delete expense with valid id is successful")
    void deleteExpense_withValidId_returnsUpdatedExpense() {

        when(userRepository.findById(any())).thenReturn(Optional.ofNullable(mockExpense.getUser()));
        when(expenseRepository.findById(any())).thenReturn(Optional.ofNullable(mockExpense));

        expenseService.deleteExpense(mockExpense.getId(), mockExpense.getUser().getId());

        verify(expenseRepository).delete(mockExpense);
    }

    @Test
    @DisplayName("Delete expense with invalid id is unsuccessful")
    void deleteExpense_withInvalidId_throwsResourceNotFoundException() {

        when(userRepository.findById(any())).thenReturn(Optional.ofNullable(mockExpense.getUser()));
        when(expenseRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () ->
                expenseService.deleteExpense(mockExpense.getId(), mockExpense.getUser().getId()));
    }
}
