package com.example.expensetracker.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ExpenseRequest(
        @NotNull Long userId,
        @NotNull @Positive @Digits(integer = 10, fraction = 2) BigDecimal amount,
        @NotBlank @Size(max = 50) String category,
        @NotNull @PastOrPresent LocalDate expenseDate,
        @Size(max = 255) String description) {
}
