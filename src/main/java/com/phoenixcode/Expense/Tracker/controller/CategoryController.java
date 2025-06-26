package com.phoenixcode.Expense.Tracker.controller;

import com.phoenixcode.Expense.Tracker.dto.CategoryResponseDto;
import com.phoenixcode.Expense.Tracker.dto.CreateCategoryRequestDto;
import com.phoenixcode.Expense.Tracker.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping
    public ResponseEntity<CategoryResponseDto> createCategory(
            @Valid @RequestBody CreateCategoryRequestDto requestDto) {

        CategoryResponseDto createdCategory = categoryService.createCategory(requestDto);
        return new ResponseEntity<>(createdCategory, HttpStatus.CREATED);
    }
}
