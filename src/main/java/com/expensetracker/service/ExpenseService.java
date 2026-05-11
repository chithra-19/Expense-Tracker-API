package com.expensetracker.service;

import com.expensetracker.dto.ExpenseSummaryDTO;
import com.expensetracker.entity.Expense;
import com.expensetracker.entity.User;
import com.expensetracker.repository.ExpenseRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ExpenseService {

    private final ExpenseRepository expenseRepository;

    public Expense saveExpense(Expense expense) {
        return expenseRepository.save(expense);
    }

    @Transactional(readOnly = true)
    public List<Expense> getExpensesByUser(User user) {
        return expenseRepository.findByUser(user);
    }

    @Transactional(readOnly = true)
    public Expense getExpenseByIdAndUser(Long id, User user) {
        return expenseRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new RuntimeException("Expense not found"));
    }

    public Expense updateExpense(Long id, Expense expense, User user) {
        Expense existing = getExpenseByIdAndUser(id, user);

        existing.setAmount(expense.getAmount());
        existing.setCategory(expense.getCategory());
        existing.setDescription(expense.getDescription());
        existing.setDate(expense.getDate());

        return expenseRepository.save(existing);
    }

    public void deleteExpense(Long id, User user) {
        Expense existing = getExpenseByIdAndUser(id, user);
        expenseRepository.delete(existing);
    }

    public List<ExpenseSummaryDTO> getCategorySummary(User user) {
        return expenseRepository.totalByCategory(user)
                .stream()
                .map(row -> new ExpenseSummaryDTO(
                        (String) row[0],
                        new BigDecimal(row[1].toString())
                ))
                .toList();
    }

    public List<ExpenseSummaryDTO> getMonthlySummary(User user) {
        return expenseRepository.totalByMonth(user)
                .stream()
                .map(row -> new ExpenseSummaryDTO(
                        row[0].toString(),
                        new BigDecimal(row[1].toString())
                ))
                .toList();
    }

    public BigDecimal getTotalByDateRange(User user, LocalDate start, LocalDate end) {
        return new BigDecimal(
                expenseRepository.totalByDateRange(user, start, end).toString()
        );
    }
}