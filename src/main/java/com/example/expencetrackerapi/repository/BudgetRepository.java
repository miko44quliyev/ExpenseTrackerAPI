package com.example.expencetrackerapi.repository;

import com.example.expencetrackerapi.entity.Budget;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BudgetRepository extends JpaRepository<Budget, Long> {

  List<Budget> findByCategoryId(Long categoryId);
}
