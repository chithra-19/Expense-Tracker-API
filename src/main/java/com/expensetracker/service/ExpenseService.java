package com.expensetracker.service;

import com.expensetracker.dto.ExpenseSummaryDTO;
import com.expensetracker.entity.Expense;
import com.expensetracker.entity.User;
import com.expensetracker.repository.ExpenseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExpenseService {

    private final ExpenseRepository expenseRepository;

    public ExpenseService(ExpenseRepository expenseRepository) { 
        this.expenseRepository = expenseRepository;
    }

    // ===================== CRUD =====================

    // CREATE
    public Expense saveExpense(Expense expense) {
        return expenseRepository.save(expense);
    }

    // READ ALL (user-specific)
    public List<Expense> getExpensesByUser(User user) {
        return expenseRepository.findByUser(user);
    }

    // READ ONE (secure)
    public Expense getExpenseByIdAndUser(Long id, User user) {
        return expenseRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new RuntimeException("Expense not found"));
    }

    // UPDATE
    public Expense updateExpense(Long id, Expense expense, User user) {
        Expense existing = getExpenseByIdAndUser(id, user);

        existing.setAmount(expense.getAmount());
        existing.setCategory(expense.getCategory());
        existing.setDescription(expense.getDescription());
        existing.setDate(expense.getDate());

        return expenseRepository.save(existing);
    }

    // DELETE
    public void deleteExpense(Long id, User user) {
        Expense existing = getExpenseByIdAndUser(id, user);
        expenseRepository.delete(existing);
    }

    // ===================== SUMMARY =====================

 // Total by category
    public List<ExpenseSummaryDTO> getCategorySummary(User user) {
        return expenseRepository.totalByCategory(user)
                .stream()
                .map(row -> new ExpenseSummaryDTO(
                        (String) row[0],                        // category name
                        BigDecimal.valueOf((Double) row[1])     // convert Double → BigDecimal
                ))
                .toList();
    }

    // Total by month
    public List<ExpenseSummaryDTO> getMonthlySummary(User user) {
        return expenseRepository.totalByMonth(user)
                .stream()
                .map(row -> new ExpenseSummaryDTO(
                        row[0].toString(),                       // month as string
                        BigDecimal.valueOf((Double) row[1])     // convert Double → BigDecimal
                ))
                .toList();
    }



    // Total for date range
    public Double getTotalByDateRange(User user, LocalDate start, LocalDate end) {
        return expenseRepository.totalByDateRange(user, start, end);
    }
}
