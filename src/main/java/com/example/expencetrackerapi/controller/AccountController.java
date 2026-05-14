package com.example.expencetrackerapi.controller;

import com.example.expencetrackerapi.dto.request.CreateAccountRequest;
import com.example.expencetrackerapi.dto.request.UpdateAccountRequest;
import com.example.expencetrackerapi.dto.response.AccountResponse;
import com.example.expencetrackerapi.dto.response.AccountResponseSummary;
import com.example.expencetrackerapi.entity.Account;
import com.example.expencetrackerapi.service.AccountService;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {
  private final AccountService accountService;

  public AccountController(AccountService accountService) {
    this.accountService = accountService;
  }

  @GetMapping
  public List<AccountResponseSummary> findAll() {
    return accountService.findAll();
  }

  @GetMapping("/{id}")
  public Account findById(@PathVariable Long id) {
    return accountService.findById(id);
  }

  @PostMapping
  public ResponseEntity<AccountResponse> create(@RequestBody CreateAccountRequest request) {
    return ResponseEntity.status(HttpStatus.CREATED).body(accountService.create(request));
  }

  @PutMapping("/{id}")
  public AccountResponse update(@PathVariable Long id, @RequestBody UpdateAccountRequest request) {
    return accountService.update(id, request);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    accountService.delete(id);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }
}
