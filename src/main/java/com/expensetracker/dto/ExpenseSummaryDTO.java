package com.expensetracker.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ExpenseSummaryDTO {

    private BigDecimal amount;  // ✅ important
    private String category;
    private LocalDate date;

    public ExpenseSummaryDTO(String category, BigDecimal amount) {
        this.category = category;
        this.amount = amount;
    }

    // ✅ Constructor if needed for date-based summaries
    public ExpenseSummaryDTO(String category, BigDecimal amount, LocalDate date) {
        this.category = category;
        this.amount = amount;
        this.date = date;
    }

    // Getters & setters
    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}

