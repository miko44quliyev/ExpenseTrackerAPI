package com.example.expencetrackerapi.mapper;


import com.example.expencetrackerapi.dto.response.CategoryResponse;
import com.example.expencetrackerapi.entity.Category;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {
    public CategoryResponse toResponse(Category category){
        CategoryResponse response = new CategoryResponse();
        response.setId(category.getId());
        response.setName(category.getName());
        response.setDescription(category.getDescription());
        response.setAccountId(category.getAccount().getId());

        return response;
    }
}
