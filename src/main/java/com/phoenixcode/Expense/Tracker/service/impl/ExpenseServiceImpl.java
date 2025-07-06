package com.phoenixcode.Expense.Tracker.service.impl;

import com.phoenixcode.Expense.Tracker.dto.CreateExpenseRequestDto;
import com.phoenixcode.Expense.Tracker.dto.ExpenseResponseDto;
import com.phoenixcode.Expense.Tracker.entity.Category;
import com.phoenixcode.Expense.Tracker.entity.Expense;
import com.phoenixcode.Expense.Tracker.entity.User;
import com.phoenixcode.Expense.Tracker.exception.ResourceNotFoundException;
import com.phoenixcode.Expense.Tracker.repository.CategoryRepository;
import com.phoenixcode.Expense.Tracker.repository.ExpenseRepository;
import com.phoenixcode.Expense.Tracker.repository.UserRepository;
import com.phoenixcode.Expense.Tracker.service.ExpenseService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ExpenseServiceImpl implements ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;

    public ExpenseServiceImpl(ExpenseRepository expenseRepository, UserRepository userRepository,
                              CategoryRepository categoryRepository, ModelMapper modelMapper) {
        this.expenseRepository = expenseRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public ExpenseResponseDto createExpense(CreateExpenseRequestDto requestDto) {
        User user = userRepository.findById(requestDto.getUser())
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found with id " + requestDto.getUser()));

        Category category = categoryRepository.findById(requestDto.getCategory())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Category not found with id " + requestDto.getCategory()));

        Expense expense = modelMapper.map(requestDto, Expense.class);
        expense.setUser(user);
        expense.setCategory(category);

        expenseRepository.save(expense);
        return modelMapper.map(expense, ExpenseResponseDto.class);
    }

    @Override
    public List<ExpenseResponseDto> getExpenses(UUID userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + userId));

        List<Expense> expenses = expenseRepository.findAllByUser(user);

        return expenses.stream()
                .map(expense -> modelMapper.map(expense, ExpenseResponseDto.class))
                .toList();
    }
}
