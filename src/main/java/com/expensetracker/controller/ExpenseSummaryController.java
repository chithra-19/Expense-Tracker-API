package com.expensetracker.controller;

import com.expensetracker.dto.ExpenseResponseDTO;
import com.expensetracker.dto.ExpenseSummaryDTO;
import com.expensetracker.entity.User;
import com.expensetracker.mapper.ExpenseMapper;
import com.expensetracker.service.ExpenseService;
import com.expensetracker.service.UserService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/expenses/summary")
public class ExpenseSummaryController {

    private final ExpenseService expenseService;
    private final UserService userService;

    // Explicit constructor
    public ExpenseSummaryController(ExpenseService expenseService, UserService userService) {
        this.expenseService = expenseService;
        this.userService = userService;
    }

    // ✅ Category-wise summary
    @GetMapping("/category")
    public ResponseEntity<List<ExpenseResponseDTO>> categorySummary(Authentication authentication) {
        User user = getUser(authentication);
        List<ExpenseSummaryDTO> summary = expenseService.getCategorySummary(user);

        // Map summary DTOs → response DTOs
        List<ExpenseResponseDTO> response = summary.stream()
                .map(ExpenseMapper::toDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    // ✅ Month-wise summary
    @GetMapping("/month")
    public ResponseEntity<List<ExpenseResponseDTO>> monthlySummary(Authentication authentication) {
        User user = getUser(authentication);
        List<ExpenseSummaryDTO> summary = expenseService.getMonthlySummary(user);

        // Map summary DTOs → response DTOs
        List<ExpenseResponseDTO> response = summary.stream()
                .map(ExpenseMapper::toDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    // ✅ Total expense for a custom date range
    @GetMapping("/summary")
    public ResponseEntity<BigDecimal> summaryByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
            Authentication authentication) {

        if (from.isAfter(to)) {
            return ResponseEntity.badRequest().body(BigDecimal.ZERO);
        }

        User user = getUser(authentication);

        // Convert Double → BigDecimal
        Double totalDouble = expenseService.getTotalByDateRange(user, from, to);
        BigDecimal total = BigDecimal.valueOf(totalDouble);

        return ResponseEntity.ok(total);
    }


    // 🔒 Helper method to fetch logged-in user
    private User getUser(Authentication authentication) {
        return userService.findByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}
