package com.example.expencetrackerapi.dto.response;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
public class BudgetResponse {

  private Long id;
  private Long categoryId;
  private Long AccountId;
  private String categoryName;
  private BigDecimal amountLimit;
  private LocalDate startDate;
  private LocalDate endDate;
  private LocalDateTime createdAt;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getCategoryId() {
    return categoryId;
  }

  public void setCategoryId(Long categoryId) {
    this.categoryId = categoryId;
  }

  public String getCategoryName() {
    return categoryName;
  }

  public void setCategoryName(String categoryName) {
    this.categoryName = categoryName;
  }

  public BigDecimal getAmountLimit() {
    return amountLimit;
  }

  public void setAmountLimit(BigDecimal amountLimit) {
    this.amountLimit = amountLimit;
  }

  public LocalDate getStartDate() {
    return startDate;
  }

  public void setStartDate(LocalDate startDate) {
    this.startDate = startDate;
  }

  public LocalDate getEndDate() {
    return endDate;
  }

  public void setEndDate(LocalDate endDate) {
    this.endDate = endDate;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public Long getAccountId() {
    return AccountId;
  }

  public void setAccountId(Long accountId) {
    AccountId = accountId;
  }
}
