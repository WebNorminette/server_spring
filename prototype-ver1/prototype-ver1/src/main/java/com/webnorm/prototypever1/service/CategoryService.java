package com.webnorm.prototypever1.service;

import com.webnorm.prototypever1.entity.category.Category;
import com.webnorm.prototypever1.exception.exceptions.BusinessLogicException;
import com.webnorm.prototypever1.exception.exceptions.CategoryException;
import com.webnorm.prototypever1.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    /*
    * [카테고리 생성]
    * # 카테고리 이름 중복체크
    */
    public Category createCategory(Category category) {
        Optional<Category> findCategory = categoryRepository.findByName(category.getName());
        if(findCategory.isPresent()) throw new BusinessLogicException(CategoryException.CATEGORY_NAME_DUP);
        return categoryRepository.save(category);
    }

    public Category findByName(String name) {
        Optional<Category> findCategory = categoryRepository.findByName(name);
        if(findCategory.isEmpty()) throw new BusinessLogicException(CategoryException.CATEGORY_NOT_FOUND);
        return findCategory.get();
    }
}
