package com.example.expencetrackerapi.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExpenseResponseSummary {
  private String title;
  private BigDecimal amount;
  private LocalDate expenseDate;
}
