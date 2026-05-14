package com.example.expencetrackerapi.service;

import com.example.expencetrackerapi.dto.request.CategoryRequest;
import com.example.expencetrackerapi.dto.response.CategoryResponse;
import com.example.expencetrackerapi.entity.Account;
import com.example.expencetrackerapi.entity.Category;
import com.example.expencetrackerapi.exception.ResourceNotFoundException;
import com.example.expencetrackerapi.mapper.CategoryMapper;
import com.example.expencetrackerapi.repository.AccountRepository;
import com.example.expencetrackerapi.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final AccountService accountService;
    private final CategoryMapper categoryMapper;
    private final AccountRepository accountRepository;

    public CategoryService(CategoryRepository categoryRepository, AccountService accountService, CategoryMapper categoryMapper, AccountRepository accountRepository) {
        this.categoryRepository = categoryRepository;
        this.accountService = accountService;
        this.categoryMapper = categoryMapper;
        this.accountRepository = accountRepository;
    }

    public List<CategoryResponse> findAll(){
        List<Category> categories = categoryRepository.findAll();
        List<CategoryResponse> responses = new ArrayList<>();

        for(Category category: categories){
            responses.add(categoryMapper.toResponse(category));
        }

        return responses;
    }

    public CategoryResponse findById(Long id){
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category with this is id is not found: " + id));
        return categoryMapper.toResponse(category);
    }

    public CategoryResponse create(CategoryRequest request){
        Account account = accountRepository.findById(request.getAccountId())
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with id: " + request.getAccountId()));

        Category category = new Category();
        category.setName(request.getName());
        category.setAccount(account);
        category.setDescription(request.getDescription());
        return categoryMapper.toResponse(categoryRepository.save(category));
    }

    public CategoryResponse update(Long id, CategoryRequest request){
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));

        if(request.getAccountId() != null){
            Account account = accountRepository.findById(request.getAccountId())
                            .orElseThrow(() -> new ResourceNotFoundException("Account not found with id: " + request.getAccountId()));

            category.setAccount(account);
        }
        if(request.getName() != null && !request.getName().isBlank()){
            category.setName(request.getName());
        }
        if(request.getDescription() != null && !request.getDescription().isBlank()){
            category.setDescription(request.getDescription());
        }

        categoryRepository.save(category);
        return categoryMapper.toResponse(category);
    }

    public void delete(Long id){
        Category category = categoryRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));

        categoryRepository.delete(category);
    }
}
