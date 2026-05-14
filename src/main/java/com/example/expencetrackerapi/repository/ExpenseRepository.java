package com.example.expencetrackerapi.repository;

import com.example.expencetrackerapi.entity.Expense;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExpenseRepository extends JpaRepository<Expense,Long> {

}
