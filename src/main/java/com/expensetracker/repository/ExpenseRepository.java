package com.expensetracker.repository;

import com.expensetracker.entity.Expense;
import com.expensetracker.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {

    List<Expense> findByUser(User user);

    Optional<Expense> findByIdAndUser(Long id, User user);

    // ===== SUMMARY QUERIES =====

    @Query("""
        SELECT e.category, SUM(e.amount)
        FROM Expense e
        WHERE e.user = :user
        GROUP BY e.category
    """)
    List<Object[]> totalByCategory(User user);

    @Query("""
        SELECT FUNCTION('YEAR_MONTH', e.date), SUM(e.amount)
        FROM Expense e
        WHERE e.user = :user
        GROUP BY FUNCTION('YEAR_MONTH', e.date)
    """)
    List<Object[]> totalByMonth(User user);

    @Query("""
        SELECT COALESCE(SUM(e.amount), 0)
        FROM Expense e
        WHERE e.user = :user
          AND e.date BETWEEN :start AND :end
    """)
    Double totalByDateRange(User user, LocalDate start, LocalDate end);
}
