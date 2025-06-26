package com.phoenixcode.Expense.Tracker.service;

import com.phoenixcode.Expense.Tracker.dto.CategoryResponseDto;
import com.phoenixcode.Expense.Tracker.dto.CreateCategoryRequestDto;
import com.phoenixcode.Expense.Tracker.entity.Category;
import com.phoenixcode.Expense.Tracker.exception.ResourceAlreadyExistsException;
import com.phoenixcode.Expense.Tracker.repository.CategoryRepository;
import com.phoenixcode.Expense.Tracker.service.impl.CategoryServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    ModelMapper modelMapper;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    private Category mockCategory;

    @BeforeEach
    void init() {
        mockCategory = createMockCategory();
    }

    @Test
    @DisplayName("Create category with valid credentials successful")
    void createCategory_withValidCredentials_returnsCategory() {

        CreateCategoryRequestDto requestDto = createCategoryDto("category", "description");
        CategoryResponseDto responseDto = createCategoryResponseDto(mockCategory);
        when(modelMapper.map(requestDto, Category.class)).thenReturn(mockCategory);
        when(modelMapper.map(mockCategory, CategoryResponseDto.class)).thenReturn(responseDto);
        when(categoryRepository.existsByName(mockCategory.getName())).thenReturn(false);
        when(categoryRepository.save(mockCategory)).thenReturn(mockCategory);

        CategoryResponseDto returnedCategory = categoryService.createCategory(requestDto);

        assertAll(
                () -> assertEquals(mockCategory.getId(), returnedCategory.getId()),
                () -> assertEquals(mockCategory.getName(), returnedCategory.getName()),
                () -> assertEquals(mockCategory.getDescription(), returnedCategory.getDescription())
        );
        verify(categoryRepository).save(mockCategory);
    }

    @Test
    @DisplayName("Create category with existing name unsuccessful")
    void createCategory_withExistingName_throwsResourceAlreadyExistsException() {

        CreateCategoryRequestDto requestDto = createCategoryDto("category", "description");
        when(categoryRepository.existsByName(requestDto.getName())).thenReturn(true);

        assertThrows(ResourceAlreadyExistsException.class, () -> categoryService.createCategory(requestDto));
    }

    private Category createMockCategory() {
        return Category.builder()
                .id(UUID.randomUUID())
                .name("category")
                .description("description")
                .build();
    }

    private CreateCategoryRequestDto createCategoryDto(String name, String description) {
        return CreateCategoryRequestDto.builder()
                .name(name)
                .description(description)
                .build();
    }

    private CategoryResponseDto createCategoryResponseDto(Category category) {
        return CategoryResponseDto.builder()
                .id(category.getId())
                .name(category.getName())
                .description(category.getDescription())
                .build();
    }
}
