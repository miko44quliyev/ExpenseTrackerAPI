package com.example.expencetrackerapi.service;

import com.example.expencetrackerapi.dto.request.CategoryRequest;
import com.example.expencetrackerapi.dto.request.CategoryResponse;
import com.example.expencetrackerapi.entity.Account;
import com.example.expencetrackerapi.entity.Category;
import com.example.expencetrackerapi.exception.ResourceNotFoundException;
import com.example.expencetrackerapi.mapper.CategoryMapper;
import com.example.expencetrackerapi.repository.CategoryRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final AccountService accountService;
    private final CategoryMapper categoryMapper;

    public CategoryService(CategoryRepository categoryRepository, AccountService accountService, CategoryMapper categoryMapper) {
        this.categoryRepository = categoryRepository;
        this.accountService = accountService;
        this.categoryMapper = categoryMapper;
    }

    public List<CategoryResponse> findAll(){
        List<Category> categories = categoryRepository.findAll();
        List<CategoryResponse> responses = new ArrayList<>();

        for(Category category: categories){
            responses.add(categoryMapper.toResponse(category));
        }

        return responses;
    }

    public Category findById(Long id){
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category with this is id is not found: " + id));
    }

    public ResponseEntity<Category> create(CategoryRequest request){
        Account account = accountService.findById(request.getAccountId());

        Category category = new Category();
        category.setName(request.getName());
        category.setAccount(account);
        category.setDescription(request.getDescription());
        return ResponseEntity.status(HttpStatus.CREATED).body(categoryRepository.save(category));
    }


}
