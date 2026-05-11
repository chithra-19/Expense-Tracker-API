package com.expensetracker.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExpenseResponseDTO {

    private Long id;

    private String title;   

    private BigDecimal amount;

    private String category;

    private String description;

    private LocalDate date;
}