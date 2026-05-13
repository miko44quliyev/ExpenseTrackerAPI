package com.example.expencetrackerapi.mapper;

import com.example.expencetrackerapi.dto.response.AccountResponse;
import com.example.expencetrackerapi.dto.response.AccountResponseSummary;
import com.example.expencetrackerapi.entity.Account;
import org.springframework.stereotype.Component;

@Component
public class AccountMapper {
  public AccountResponse toResponse(Account account) {
    AccountResponse response = new AccountResponse();
    response.setId(account.getId());
    response.setFullName(account.getFullName());
    response.setEmail(account.getEmail());
    response.setCurrentBalance(account.getCurrentBalance());
    response.setCreatedAt(account.getCreatedAt());

    return response;
  }

  public AccountResponseSummary toSummary(Account account) {
    AccountResponseSummary summary = new AccountResponseSummary();
    summary.setId(account.getId());
    summary.setEmail(account.getEmail());
    summary.setFullName(account.getFullName());
    return summary;
  }
}
