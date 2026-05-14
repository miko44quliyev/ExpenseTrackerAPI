package com.example.expencetrackerapi.service;

import com.example.expencetrackerapi.dto.request.CreateBudgetRequest;
import com.example.expencetrackerapi.dto.response.BudgetResponse;
import com.example.expencetrackerapi.entity.Budget;
import com.example.expencetrackerapi.entity.Category;
import com.example.expencetrackerapi.exception.ResourceNotFoundException;
import com.example.expencetrackerapi.mapper.BudgetMapper;
import com.example.expencetrackerapi.repository.BudgetRepository;
import com.example.expencetrackerapi.repository.CategoryRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BudgetService {

    private final BudgetRepository budgetRepository;
    private final CategoryRepository categoryRepository;
    private final BudgetMapper budgetMapper;

    public BudgetService(BudgetRepository budgetRepository,
                         CategoryRepository categoryRepository,
                         BudgetMapper budgetMapper) {
        this.budgetRepository = budgetRepository;
        this.categoryRepository = categoryRepository;
        this.budgetMapper = budgetMapper;
    }

    @Transactional
    public BudgetResponse create(CreateBudgetRequest request) {
        Budget budget = new Budget();
        budget.setAmountLimit(request.getAmountLimit());
        budget.setStartDate(request.getStartDate());
        budget.setEndDate(request.getEndDate());

        if (request.getCategoryId() != null) {
            Category category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + request.getCategoryId()));
            budget.setCategory(category);
        }

        Budget saved = budgetRepository.save(budget);
        return budgetMapper.toResponse(saved);
    }


    public List<BudgetResponse> getAll() {
        List<Budget> budgets = budgetRepository.findAll();
        List<BudgetResponse> responses = new ArrayList<>();
        for (Budget budget : budgets) {
            responses.add(budgetMapper.toResponse(budget));
        }
        return responses;
    }

    public BudgetResponse getById(Long id) {
        Budget budget = budgetRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Budget not found with id: " + id));
        return budgetMapper.toResponse(budget);
    }

    @Transactional
    public BudgetResponse update(Long id, CreateBudgetRequest request) {
        Budget budget = budgetRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Budget not found with id: " + id));

        budget.setAmountLimit(request.getAmountLimit());
        budget.setStartDate(request.getStartDate());
        budget.setEndDate(request.getEndDate());

        if (request.getCategoryId() != null) {
            Category category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + request.getCategoryId()));
            budget.setCategory(category);
        }

        return budgetMapper.toResponse(budgetRepository.save(budget));
    }

    @Transactional
    public void delete(Long id) {
        if (!budgetRepository.existsById(id)) {
            throw new ResourceNotFoundException("Budget not found with id: " + id);
        }
        budgetRepository.deleteById(id);
    }
}