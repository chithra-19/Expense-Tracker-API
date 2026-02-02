package com.expensetracker.mapper;

import com.expensetracker.dto.ExpenseRequestDTO;
import com.expensetracker.dto.ExpenseResponseDTO;
import com.expensetracker.dto.ExpenseSummaryDTO;
import com.expensetracker.entity.Category;
import com.expensetracker.entity.Expense;

public class ExpenseMapper {

    // Convert ExpenseRequestDTO → Expense entity
    public static Expense toEntity(ExpenseRequestDTO dto, Category categoryEntity) {
        Expense expense = new Expense();
        expense.setAmount(dto.getAmount());
        expense.setDate(dto.getDate());
        expense.setDescription(dto.getDescription());
        expense.setCategory(categoryEntity); // ✅ set Category entity
        return expense;
    }

    // Convert Expense entity → ExpenseResponseDTO
    public static ExpenseResponseDTO toDto(Expense expense) {
        ExpenseResponseDTO dto = new ExpenseResponseDTO();
        dto.setId(expense.getId());
        dto.setAmount(expense.getAmount());
        dto.setCategory(expense.getCategory() != null ? expense.getCategory().getName() : null);
        dto.setDescription(expense.getDescription());
        dto.setDate(expense.getDate());
        return dto;
    }

    // Convert ExpenseSummaryDTO → ExpenseResponseDTO (for summaries)
    public static ExpenseResponseDTO toDto(ExpenseSummaryDTO summary) {
        ExpenseResponseDTO dto = new ExpenseResponseDTO();
        dto.setAmount(summary.getAmount());
        dto.setCategory(summary.getCategory());
        dto.setDate(summary.getDate());
        dto.setDescription(null); // optional
        dto.setId(null);
        return dto;
    }
}
