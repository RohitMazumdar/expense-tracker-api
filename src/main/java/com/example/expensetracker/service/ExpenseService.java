package com.example.expensetracker.service;

import com.example.expensetracker.dto.CategorySummary;
import com.example.expensetracker.dto.ExpenseRequest;
import com.example.expensetracker.dto.ExpenseResponse;
import com.example.expensetracker.entity.Expense;
import com.example.expensetracker.entity.User;
import com.example.expensetracker.exception.ResourceNotFoundException;
import com.example.expensetracker.repository.ExpenseRepository;
import com.example.expensetracker.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.YearMonth;
import java.util.List;

@Service
public class ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final UserRepository userRepository;

    public ExpenseService(ExpenseRepository expenseRepository, UserRepository userRepository) {
        this.expenseRepository = expenseRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public ExpenseResponse create(ExpenseRequest request) {
        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new ResourceNotFoundException("User", request.userId()));
        Expense expense = new Expense(user, request.amount(), request.category().trim(),
                request.expenseDate(), request.description());
        return ExpenseResponse.from(expenseRepository.save(expense));
    }

    @Transactional(readOnly = true)
    public ExpenseResponse getById(Long id) {
        return ExpenseResponse.from(findOrThrow(id));
    }

    // Update: load the entity, change its fields; JPA "dirty checking" saves it when the transaction commits.
    // Note: the owner (userId) of an existing expense is not changed by an update.
    @Transactional
    public ExpenseResponse update(Long id, ExpenseRequest request) {
        Expense expense = findOrThrow(id);
        expense.setAmount(request.amount());
        expense.setCategory(request.category().trim());
        expense.setExpenseDate(request.expenseDate());
        expense.setDescription(request.description());
        return ExpenseResponse.from(expense);
    }

    @Transactional
    public void delete(Long id) {
        expenseRepository.delete(findOrThrow(id));
    }

    @Transactional(readOnly = true)
    public Page<ExpenseResponse> list(Long userId, String category, int page, int size) {
        requireUser(userId);
        // Newest first; id as tie-breaker so paging order is stable.
        Pageable pageable = PageRequest.of(page, size,
                Sort.by(Sort.Order.desc("expenseDate"), Sort.Order.desc("id")));

        Page<Expense> result = (category == null || category.isBlank())
                ? expenseRepository.findByUserId(userId, pageable)
                : expenseRepository.findByUserIdAndCategoryIgnoreCase(userId, category.trim(), pageable);
        return result.map(ExpenseResponse::from);
    }

    @Transactional(readOnly = true)
    public List<CategorySummary> monthlySummary(Long userId, YearMonth month) {
        requireUser(userId);
        return expenseRepository.summarizeByCategory(userId, month.atDay(1), month.atEndOfMonth());
    }

    private void requireUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User", userId);
        }
    }

    private Expense findOrThrow(Long id) {
        return expenseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Expense", id));
    }
}
