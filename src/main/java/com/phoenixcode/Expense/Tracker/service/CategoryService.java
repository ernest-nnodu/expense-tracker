package com.phoenixcode.Expense.Tracker.service;

import com.phoenixcode.Expense.Tracker.dto.CategoryResponseDto;
import com.phoenixcode.Expense.Tracker.dto.CreateCategoryRequestDto;

public interface CategoryService {
    CategoryResponseDto createCategory(CreateCategoryRequestDto requestDto);
}
