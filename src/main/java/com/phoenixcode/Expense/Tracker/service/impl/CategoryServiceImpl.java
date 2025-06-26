package com.phoenixcode.Expense.Tracker.service.impl;

import com.phoenixcode.Expense.Tracker.dto.CategoryResponseDto;
import com.phoenixcode.Expense.Tracker.dto.CreateCategoryRequestDto;
import com.phoenixcode.Expense.Tracker.entity.Category;
import com.phoenixcode.Expense.Tracker.exception.ResourceAlreadyExistsException;
import com.phoenixcode.Expense.Tracker.repository.CategoryRepository;
import com.phoenixcode.Expense.Tracker.service.CategoryService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;

    public CategoryServiceImpl(CategoryRepository categoryRepository, ModelMapper modelMapper) {
        this.categoryRepository = categoryRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public CategoryResponseDto createCategory(CreateCategoryRequestDto requestDto) {

        if (categoryRepository.existsByName(requestDto.getName())) {
            throw new ResourceAlreadyExistsException("Category already exits with name " + requestDto.getName());
        }

        Category categoryToSave = modelMapper.map(requestDto, Category.class);
        Category savedCategory = categoryRepository.save(categoryToSave);

        return modelMapper.map(savedCategory, CategoryResponseDto.class);
    }
}
