package com.example.expencetrackerapi.service;

import com.example.expencetrackerapi.dto.request.CreateExpenseRequest;
import com.example.expencetrackerapi.dto.request.UpdateExpenseRequest;
import com.example.expencetrackerapi.dto.response.ExpenseResponse;
import com.example.expencetrackerapi.dto.response.ExpenseResponseSummary;
import com.example.expencetrackerapi.entity.Account;
import com.example.expencetrackerapi.entity.Budget;
import com.example.expencetrackerapi.entity.Category;
import com.example.expencetrackerapi.entity.Expense;
import com.example.expencetrackerapi.exception.InsufficientFundsException;
import com.example.expencetrackerapi.exception.ResourceNotFoundException;
import com.example.expencetrackerapi.mapper.ExpenseMapper;
import com.example.expencetrackerapi.repository.AccountRepository;
import com.example.expencetrackerapi.repository.BudgetRepository;
import com.example.expencetrackerapi.repository.CategoryRepository;
import com.example.expencetrackerapi.repository.ExpenseRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
@Service
public class ExpenseService {
    private final ExpenseRepository expenseRepository;
    private final AccountRepository accountRepository;
    private final BudgetRepository budgetRepository;
    private final CategoryRepository categoryRepository;
    private final ExpenseMapper expenseMapper;

    public ExpenseService(ExpenseRepository expenseRepository, AccountRepository accountRepository, BudgetRepository budgetRepository, CategoryRepository categoryRepository, ExpenseMapper expenseMapper) {
        this.expenseRepository = expenseRepository;
        this.accountRepository = accountRepository;
        this.budgetRepository = budgetRepository;
        this.categoryRepository = categoryRepository;
        this.expenseMapper = expenseMapper;
    }

    @Transactional
    public ExpenseResponse create(CreateExpenseRequest request) {
        Account account = accountRepository.findById(request.getAccountId()).orElseThrow(() -> new ResourceNotFoundException("Account not found with id"));
        if (account.getCurrentBalance().compareTo(request.getAmount()) < 0) {
            throw new InsufficientFundsException(
                    String.format("Insufficient funds. Current balance: %s, Required: %s",
                            account.getCurrentBalance(), request.getAmount())
            );
        }
        Category category = categoryRepository.findById(request.getCategoryId()).orElseThrow(() -> new ResourceNotFoundException("Category not found with id"));
        Budget budget = null;
        if (request.getBudgetId() != null) {
            budget = budgetRepository.findById(request.getBudgetId())
                    .orElseThrow(() -> new ResourceNotFoundException("Budget not found"));
        }
        account.setCurrentBalance(account.getCurrentBalance().subtract(request.getAmount()));
        accountRepository.save(account);
        Expense expense = expenseMapper.toEntity(request, account, category, budget);
        return expenseMapper.toResponse(expenseRepository.save(expense));
    }
    @Transactional
    public ExpenseResponse update(UpdateExpenseRequest request, Long id) {
        Expense existingExpense = expenseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Expense not found with id: " + id));
        Account account = existingExpense.getAccount();
        BigDecimal restoredBalance = account.getCurrentBalance().add(existingExpense.getAmount());

        if (restoredBalance.compareTo(request.getAmount()) < 0) {
            throw new InsufficientFundsException("Insufficient funds for this update.");
        }
        account.setCurrentBalance(restoredBalance.subtract(request.getAmount()));
        accountRepository.save(account);
        expenseMapper.updateEntity(existingExpense,request);
        return expenseMapper.toResponse(expenseRepository.save(existingExpense));

    }
    public List<ExpenseResponseSummary> getAll() {
        return expenseRepository.findAll()
                .stream()
                .map(expenseMapper::toSummary)
                .collect(Collectors.toList());
    }

    public ExpenseResponse getById(Long id) {
        Expense expense = expenseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Expense not found with id: " + id));
        return expenseMapper.toResponse(expense);
    }

    @Transactional
    public void delete(Long id) {
        Expense expense = expenseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Expense not found with id: " + id));
        Account account = expense.getAccount();
        account.setCurrentBalance(account.getCurrentBalance().add(expense.getAmount()));
        accountRepository.save(account);
        expenseRepository.delete(expense);
    }
}


