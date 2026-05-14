package com.example.expencetrackerapi.mapper;

import com.example.expencetrackerapi.dto.response.BudgetResponse;
import com.example.expencetrackerapi.entity.Budget;
import org.springframework.stereotype.Component;

@Component
public class BudgetMapper {

    public BudgetResponse toResponse(Budget budget) {
        BudgetResponse response = new BudgetResponse();
        response.setId(budget.getId());
        response.setAmountLimit(budget.getAmountLimit());
        response.setStartDate(budget.getStartDate());
        response.setEndDate(budget.getEndDate());
        response.setCreatedAt(budget.getCreatedAt());

        if (budget.getCategory() != null) {
            response.setCategoryId(budget.getCategory().getId());
            response.setCategoryName(budget.getCategory().getName());

            if (budget.getCategory().getAccount() != null) {
                response.setAccountId(budget.getCategory().getAccount().getId());
            }
        }

        return response;
    }
}