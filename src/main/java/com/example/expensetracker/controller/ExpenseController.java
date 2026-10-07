package com.example.expensetracker.controller;

import com.example.expensetracker.dto.CategorySummary;
import com.example.expensetracker.dto.ExpenseRequest;
import com.example.expensetracker.dto.ExpenseResponse;
import com.example.expensetracker.service.ExpenseService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.YearMonth;
import java.util.List;

// The controller only handles HTTP concerns; business logic lives in the service.
@Validated
@RestController
@RequestMapping("/expenses")
public class ExpenseController {

    private final ExpenseService expenseService;

    public ExpenseController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ExpenseResponse create(@Valid @RequestBody ExpenseRequest request) {
        return expenseService.create(request);
    }

    @GetMapping("/{id}")
    public ExpenseResponse get(@PathVariable Long id) {
        return expenseService.getById(id);
    }

    @PutMapping("/{id}")
    public ExpenseResponse update(@PathVariable Long id, @Valid @RequestBody ExpenseRequest request) {
        return expenseService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        expenseService.delete(id);
    }

    // GET /expenses?userId=1&category=Food&page=0&size=10
    @GetMapping
    public Page<ExpenseResponse> list(@RequestParam Long userId,
                                      @RequestParam(required = false) String category,
                                      @RequestParam(defaultValue = "0") @Min(0) int page,
                                      @RequestParam(defaultValue = "10") @Min(1) @Max(100) int size) {
        return expenseService.list(userId, category, page, size);
    }

    // GET /expenses/summary?userId=1&month=2026-10
    @GetMapping("/summary")
    public List<CategorySummary> summary(@RequestParam Long userId,
                                         @RequestParam @DateTimeFormat(pattern = "yyyy-MM") YearMonth month) {
        return expenseService.monthlySummary(userId, month);
    }
}
