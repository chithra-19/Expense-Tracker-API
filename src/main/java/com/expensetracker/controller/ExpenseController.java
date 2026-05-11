package com.expensetracker.controller;

import com.expensetracker.dto.ExpenseRequestDTO;
import com.expensetracker.dto.ExpenseResponseDTO;
import com.expensetracker.entity.Category;
import com.expensetracker.entity.Expense;
import com.expensetracker.entity.User;
import com.expensetracker.mapper.ExpenseMapper;
import com.expensetracker.service.CategoryService;
import com.expensetracker.service.ExpenseService;
import com.expensetracker.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/expenses")
public class ExpenseController {

    private final ExpenseService expenseService;
    private final UserService userService;
    private final CategoryService categoryService;

    // Constructor
    public ExpenseController(ExpenseService expenseService,
                             UserService userService,
                             CategoryService categoryService) {
        this.expenseService = expenseService;
        this.userService = userService;
        this.categoryService = categoryService;
    }

    // CREATE
    @PostMapping
    public ResponseEntity<ExpenseResponseDTO> addExpense(
            @Valid @RequestBody ExpenseRequestDTO request,
            Authentication authentication) {

        User user = getUser(authentication);
        Category categoryEntity = categoryService.findByName(request.getCategory())
                .orElseGet(() -> categoryService.createCategory(request.getCategory()));
        if (categoryEntity == null) {
            throw new RuntimeException("Category not found");
        }

        Expense expense = ExpenseMapper.toEntity(request, categoryEntity);
        expense.setUser(user);

        Expense saved = expenseService.saveExpense(expense);
        return ResponseEntity.status(201)
                .body(ExpenseMapper.toDto(saved));
    }

    // READ ALL
    @GetMapping
    public ResponseEntity<List<ExpenseResponseDTO>> getExpenses(Authentication authentication) {
        User user = getUser(authentication);

        List<ExpenseResponseDTO> response = expenseService.getExpensesByUser(user)
                .stream()
                .map(ExpenseMapper::toDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    // READ ONE
    @GetMapping("/{id}")
    public ResponseEntity<ExpenseResponseDTO> getExpenseById(
            @PathVariable Long id,
            Authentication authentication) {

        User user = getUser(authentication);
        Expense expense = expenseService.getExpenseByIdAndUser(id, user);

        return ResponseEntity.ok(ExpenseMapper.toDto(expense));
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<ExpenseResponseDTO> updateExpense(
            @PathVariable Long id,
            @Valid @RequestBody ExpenseRequestDTO request,
            Authentication authentication) {

        User user = getUser(authentication);
        Category categoryEntity = categoryService.findByName(request.getCategory())
                .orElseGet(() -> categoryService.createCategory(request.getCategory()));
        Expense updatedExpense = ExpenseMapper.toEntity(request, categoryEntity);
        Expense updated = expenseService.updateExpense(id, updatedExpense, user);

        return ResponseEntity.ok(ExpenseMapper.toDto(updated));
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExpense(
            @PathVariable Long id,
            Authentication authentication) {

        User user = getUser(authentication);
        expenseService.deleteExpense(id, user);
        return ResponseEntity.noContent().build();
    }

    // Helper method to get logged-in user
    private User getUser(Authentication authentication) {
        return userService.findByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}
