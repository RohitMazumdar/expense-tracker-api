package com.example.expensetracker.dto;

import com.example.expensetracker.entity.Expense;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ExpenseResponse(
        Long id,
        Long userId,
        BigDecimal amount,
        String category,
        LocalDate expenseDate,
        String description) {

    public static ExpenseResponse from(Expense e) {
        return new ExpenseResponse(e.getId(), e.getUser().getId(), e.getAmount(),
                e.getCategory(), e.getExpenseDate(), e.getDescription());
    }
}
