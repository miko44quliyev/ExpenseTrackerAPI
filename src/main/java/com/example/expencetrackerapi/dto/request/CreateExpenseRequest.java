package com.example.expencetrackerapi.dto.request;

import com.example.expencetrackerapi.entity.PaymentMethod;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateExpenseRequest {
    @NotBlank
    private String title;

    @Positive
    private BigDecimal amount;

    @NotBlank
    private LocalDate expenseDate;

    @NotNull
    private PaymentMethod paymentMethod;

    @NotNull
    private Long accountId;

    @NotNull
    private Long categoryId;

    private Long budgetId;

}
