package com.expensetracker.controller;

import com.expensetracker.dto.ExpenseResponseDTO;
import com.expensetracker.dto.ExpenseSummaryDTO;
import com.expensetracker.entity.User;
import com.expensetracker.mapper.ExpenseMapper;
import com.expensetracker.service.ExpenseService;
import com.expensetracker.service.UserService;

import lombok.RequiredArgsConstructor;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping("/expenses")
public class ExpenseSummaryController {

    private final ExpenseService expenseService;
    private final UserService userService;

    @GetMapping("/summary/category")
    public ResponseEntity<List<ExpenseSummaryDTO>> categorySummary(Authentication authentication) {
        User user = getUser(authentication);
        return ResponseEntity.ok(expenseService.getCategorySummary(user));
    }

    @GetMapping("/summary/monthly")
    public ResponseEntity<List<ExpenseSummaryDTO>> monthlySummary(Authentication authentication) {
        User user = getUser(authentication);
        return ResponseEntity.ok(expenseService.getMonthlySummary(user));
    }

    @GetMapping("/summary")
    public ResponseEntity<BigDecimal> summaryByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
            Authentication authentication) {

        if (from.isAfter(to)) {
            return ResponseEntity.badRequest().build();
        }

        User user = getUser(authentication);
        return ResponseEntity.ok(expenseService.getTotalByDateRange(user, from, to));
    }

    private User getUser(Authentication authentication) {
        return userService.findByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}