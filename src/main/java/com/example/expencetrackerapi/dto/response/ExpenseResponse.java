package com.example.expencetrackerapi.dto.response;

import com.example.expencetrackerapi.entity.PaymentMethod;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExpenseResponse {
  private String title;
  private BigDecimal amount;
  private LocalDate expenseDate;
  private PaymentMethod paymentMethod;
  private Long accountId;
  private Long categoryId;
  private Long budgetId;
}
