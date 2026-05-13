package com.example.expencetrackerapi.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountResponseSummary {
    private Long id;
    private String email;
    private String fullName;
}
