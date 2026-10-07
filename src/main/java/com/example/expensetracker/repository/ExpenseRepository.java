package com.example.expensetracker.repository;

import com.example.expensetracker.dto.CategorySummary;
import com.example.expensetracker.entity.Expense;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {

    // Spring Data derives these queries from the method names.
    Page<Expense> findByUserId(Long userId, Pageable pageable);

    Page<Expense> findByUserIdAndCategoryIgnoreCase(Long userId, String category, Pageable pageable);

    // Total spend per category in a date range: one SQL GROUP BY, done by the database.
    @Query("""
           select new com.example.expensetracker.dto.CategorySummary(e.category, sum(e.amount))
           from Expense e
           where e.user.id = :userId
             and e.expenseDate between :start and :end
           group by e.category
           order by sum(e.amount) desc
           """)
    List<CategorySummary> summarizeByCategory(@Param("userId") Long userId,
                                              @Param("start") LocalDate start,
                                              @Param("end") LocalDate end);
}
