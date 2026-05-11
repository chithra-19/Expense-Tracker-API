package com.expensetracker.service;

import com.expensetracker.entity.Category;
import com.expensetracker.repository.CategoryRepository;

import java.util.Optional;

import org.springframework.stereotype.Service;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    // Fetch category by name, throw if not found
    public Optional<Category> findByName(String name) {
        return categoryRepository.findByName(name);
    }
    
    public Category createCategory(String name) {
        Category category = new Category();
        category.setName(name);
        return categoryRepository.save(category);
    }
}
