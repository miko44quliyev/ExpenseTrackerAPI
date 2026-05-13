package com.example.expencetrackerapi.controller;

import com.example.expencetrackerapi.dto.request.CreateExpenseRequest;
import com.example.expencetrackerapi.dto.request.UpdateExpenseRequest;
import com.example.expencetrackerapi.dto.response.ExpenseResponse;
import com.example.expencetrackerapi.dto.response.ExpenseResponseSummary;
import com.example.expencetrackerapi.service.ExpenseService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/expenses") // Matching your /api/accounts style
public class ExpenseController {

  private final ExpenseService expenseService;

  public ExpenseController(ExpenseService expenseService) {
    this.expenseService = expenseService;
  }

  @GetMapping
  public List<ExpenseResponseSummary> getAll() {
    return expenseService.getAll();
  }

  @GetMapping("/{id}")
  public ExpenseResponse getById(@PathVariable Long id) {
    return expenseService.getById(id);
  }

  @PostMapping
  public ResponseEntity<ExpenseResponse> create(@RequestBody @Valid CreateExpenseRequest request) {
    return ResponseEntity.status(HttpStatus.CREATED).body(expenseService.create(request));
  }

  @PutMapping("/{id}")
  public ExpenseResponse update(@PathVariable Long id, @RequestBody UpdateExpenseRequest request) {
    return expenseService.update(request, id);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    expenseService.delete(id);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }
}
