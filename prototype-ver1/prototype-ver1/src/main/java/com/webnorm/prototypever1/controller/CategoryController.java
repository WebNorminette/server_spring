package com.webnorm.prototypever1.controller;

import com.webnorm.prototypever1.api.request.AddCategoryRequest;
import com.webnorm.prototypever1.api.response.SingleResponse;
import com.webnorm.prototypever1.entity.category.Category;
import com.webnorm.prototypever1.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/categories")
public class CategoryController {
    private final CategoryService categoryService;

    @PostMapping
    public SingleResponse addCategory(@RequestBody AddCategoryRequest request) {
        Category category = Category.builder()
                .name(request.getName())
                .build();
        Category newCategory = categoryService.createCategory(category);
        return new SingleResponse(HttpStatus.OK, "successfully added Category", newCategory);
    }
}
