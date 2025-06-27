package com.phoenixcode.Expense.Tracker.service;

import com.phoenixcode.Expense.Tracker.dto.CategoryResponseDto;
import com.phoenixcode.Expense.Tracker.dto.CreateCategoryRequestDto;

import java.util.List;

public interface CategoryService {
    CategoryResponseDto createCategory(CreateCategoryRequestDto requestDto);

    List<CategoryResponseDto> getAllCategories();
}
