package com.webnorm.prototypever1.controller;

import com.webnorm.prototypever1.api.request.AddProductRequest;
import com.webnorm.prototypever1.api.response.MultiResponse;
import com.webnorm.prototypever1.api.response.SingleResponse;
import com.webnorm.prototypever1.entity.category.Category;
import com.webnorm.prototypever1.entity.product.Product;
import com.webnorm.prototypever1.service.CategoryService;
import com.webnorm.prototypever1.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {
    private final ProductService productService;
    private final CategoryService categoryService;

    // 상품 추가
    @PostMapping
    public SingleResponse addProduct(@RequestBody AddProductRequest request) {
        Product product = Product.builder()
                .name(request.getName())
                .price(request.getPrice())
                .colors(request.getColorList())
                .details(request.getDetails())
                .shipping(request.getShipping())
                .build();
        if (request.getCategory() != null) {  // 카테고리 설정 : 카테고리가 존재하는 경우에만 설정
            Category category = categoryService.findByName(request.getCategory());
            product.specifyCategory(category);
        }
        Product newProduct = productService.createProduct(product);     // db 저장
        return new SingleResponse(HttpStatus.OK, "successfully added Product", newProduct);
    }

    // 상품 전체조회
    @GetMapping
    public MultiResponse serchAllProducts() {
        List<Product> allProducts = productService.findAll();
        return new MultiResponse(HttpStatus.OK, "successfully found all Products", allProducts);
    }

    // 키워드로 상품 조회
    @GetMapping("/search")
    public MultiResponse searchProductsByTerm(@RequestParam("term") String term) {
        List<Product> products = productService.findByTerm(term);
        return new MultiResponse(HttpStatus.OK, "successfully found Products contain " + term, products);
    }

    // 카테고리로 상품 조회
    @GetMapping("/{category}")
    public MultiResponse searchProductByCategory(@PathVariable("category") String category) {
        List<Product> products = productService.findByCategory(category);
        return new MultiResponse(HttpStatus.OK, "successfully found Products by category " + category, products);
    }
}
