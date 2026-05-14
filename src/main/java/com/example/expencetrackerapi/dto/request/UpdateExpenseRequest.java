package com.example.expencetrackerapi.dto.request;

import com.example.expencetrackerapi.entity.PaymentMethod;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateExpenseRequest {
    @NotBlank
    private String title;

    @Positive
    private BigDecimal amount;

    @NotBlank
    private LocalDate expenseDate;

    @NotBlank
    private PaymentMethod paymentMethod;
}
