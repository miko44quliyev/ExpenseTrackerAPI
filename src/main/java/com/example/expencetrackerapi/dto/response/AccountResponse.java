package com.example.expencetrackerapi.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountResponse {
    private Long id;
    private String fullName;
    private String email;
    private BigDecimal currentBalance;
    private LocalDateTime createdAt;
}
