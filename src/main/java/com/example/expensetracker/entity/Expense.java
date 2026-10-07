package com.example.expensetracker.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;

// The index supports the monthly summary query (filter by user and date range).
@Entity
@Table(name = "expenses",
       indexes = @Index(name = "idx_expense_user_date", columnList = "user_id, expense_date"))
public class Expense {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // BigDecimal (never double) for money to avoid floating-point errors.
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal amount;

    @Column(nullable = false, length = 50)
    private String category;

    @Column(name = "expense_date", nullable = false)
    private LocalDate expenseDate;

    @Column(length = 255)
    private String description;

    // Many expenses belong to one user. LAZY = the user row is only loaded if we actually need it.
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    protected Expense() { } // required by JPA

    public Expense(User user, BigDecimal amount, String category, LocalDate expenseDate, String description) {
        this.user = user;
        this.amount = amount;
        this.category = category;
        this.expenseDate = expenseDate;
        this.description = description;
    }

    public Long getId() { return id; }
    public BigDecimal getAmount() { return amount; }
    public String getCategory() { return category; }
    public LocalDate getExpenseDate() { return expenseDate; }
    public String getDescription() { return description; }
    public User getUser() { return user; }

    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public void setCategory(String category) { this.category = category; }
    public void setExpenseDate(LocalDate expenseDate) { this.expenseDate = expenseDate; }
    public void setDescription(String description) { this.description = description; }
}
