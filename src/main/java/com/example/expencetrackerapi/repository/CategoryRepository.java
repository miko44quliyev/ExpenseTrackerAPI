package com.example.expencetrackerapi.repository;

import com.example.expencetrackerapi.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
