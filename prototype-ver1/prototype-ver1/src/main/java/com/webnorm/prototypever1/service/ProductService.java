package com.webnorm.prototypever1.service;

import com.webnorm.prototypever1.entity.category.Category;
import com.webnorm.prototypever1.entity.product.Product;
import com.webnorm.prototypever1.exception.exceptions.BusinessLogicException;
import com.webnorm.prototypever1.exception.exceptions.CategoryException;
import com.webnorm.prototypever1.exception.exceptions.ProductException;
import com.webnorm.prototypever1.repository.CategoryRepository;
import com.webnorm.prototypever1.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    /*
    * [상품 저장]
    * # 상품명 중복검사
    */
    public Product createProduct(Product product) {
        Optional<Product> findProduct = productRepository.findByName(product.getName());
        if (findProduct.isPresent())
            throw new BusinessLogicException(ProductException.PRODUCT_NAME_DUP);
        return productRepository.save(product);
    }

    /*
     * [전체 상품 조회]
     */
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    /*
    * [키워드로 조회]
    */
    public List<Product> findByTerm(String term) {
        List<Product> products = productRepository.findByTerm(term)
                .stream()
                .toList();
        return products;
    }

    /*
     * [카테고리로 조회]
     */
    public List<Product> findByCategory(String category) {
        Optional<Category> findCategory = categoryRepository.findByName(category);
        if (findCategory.isEmpty()) throw new BusinessLogicException(CategoryException.CATEGORY_NOT_FOUND);
        return productRepository.findByCategory(category);
    }
}
