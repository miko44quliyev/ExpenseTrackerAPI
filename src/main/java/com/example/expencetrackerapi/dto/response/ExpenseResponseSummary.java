package com.example.expencetrackerapi.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExpenseResponseSummary {
    private String title;
    private BigDecimal amount;
    private LocalDate expenseDate;
}
