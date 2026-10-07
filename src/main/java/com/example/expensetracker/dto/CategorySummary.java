package com.example.expensetracker.dto;

import java.math.BigDecimal;

public record CategorySummary(String category, BigDecimal total) {
}
