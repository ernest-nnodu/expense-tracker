package com.phoenixcode.Expense.Tracker.repository;

import com.phoenixcode.Expense.Tracker.entity.Expense;
import com.phoenixcode.Expense.Tracker.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, UUID> {
    List<Expense> findAllByUser(User user);
}
