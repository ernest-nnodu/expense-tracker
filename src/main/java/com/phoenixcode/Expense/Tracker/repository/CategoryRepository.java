package com.phoenixcode.Expense.Tracker.repository;

import com.phoenixcode.Expense.Tracker.entity.Category;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CategoryRepository extends JpaRepository<Category, UUID> {
    boolean existsByName(@NotBlank String name);
}
