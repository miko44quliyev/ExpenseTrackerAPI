package com.example.expencetrackerapi.mapper;

import com.example.expencetrackerapi.dto.request.CreateExpenseRequest;
import com.example.expencetrackerapi.dto.request.UpdateExpenseRequest;
import com.example.expencetrackerapi.dto.response.ExpenseResponse;
import com.example.expencetrackerapi.dto.response.ExpenseResponseSummary;
import com.example.expencetrackerapi.entity.Account;
import com.example.expencetrackerapi.entity.Budget;
import com.example.expencetrackerapi.entity.Category;
import com.example.expencetrackerapi.entity.Expense;
import org.springframework.stereotype.Component;

@Component
public class ExpenseMapper {
    public ExpenseResponse toResponse(Expense expense){
        ExpenseResponse response = new ExpenseResponse();
        response.setAmount(expense.getAmount());
        response.setTitle(expense.getTitle());
        response.setExpenseDate(expense.getExpenseDate());
        response.setPaymentMethod(expense.getPaymentMethod());
        response.setAccountId(expense.getAccount().getId());
        response.setCategoryId(expense.getCategory().getId());
        response.setBudgetId(expense.getBudget().getId());
        return response;
    }


    public ExpenseResponseSummary toSummary(Expense expense){
        ExpenseResponseSummary summary = new ExpenseResponseSummary();
        summary.setAmount(expense.getAmount());
        summary.setExpenseDate(expense.getExpenseDate());
        summary.setTitle(expense.getTitle());
        return summary;
    }
    public Expense toEntity(CreateExpenseRequest request, Account account, Category category, Budget budget) {
        Expense expense = new Expense();
        expense.setTitle(request.getTitle());
        expense.setAmount(request.getAmount());
        expense.setExpenseDate(request.getExpenseDate());
        expense.setPaymentMethod(request.getPaymentMethod());
        expense.setAccount(account);
        expense.setCategory(category);
        if (budget != null) {
            expense.setBudget(budget);
        }

        return expense;
    }
    public void updateEntity(Expense expense ,UpdateExpenseRequest request) {
        if(request.getTitle()!=null){
            expense.setTitle(request.getTitle());
        }
        if(request.getAmount()!=null){
            expense.setAmount(request.getAmount());
        }
        if(request.getExpenseDate()!=null){
            expense.setExpenseDate(request.getExpenseDate());
        }
        if(request.getPaymentMethod()!=null){
            expense.setPaymentMethod(request.getPaymentMethod());
        }
    }
}
