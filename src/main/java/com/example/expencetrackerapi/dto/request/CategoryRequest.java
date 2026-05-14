package com.example.expencetrackerapi.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryRequest {

    @NotNull
    private Long accountId;

    @NotBlank
    @Size(min = 2, max = 100, message = "Name must be 2-100 characters!")
    private String name;

    @Size(max = 500)
    private String description;
}
