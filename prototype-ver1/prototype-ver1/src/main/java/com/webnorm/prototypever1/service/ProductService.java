package com.webnorm.prototypever1.service;

import com.webnorm.prototypever1.entity.product.Collection;
import com.webnorm.prototypever1.entity.product.Image;
import com.webnorm.prototypever1.entity.product.Product;
import com.webnorm.prototypever1.exception.exceptions.BusinessLogicException;
import com.webnorm.prototypever1.exception.exceptions.CollectionException;
import com.webnorm.prototypever1.exception.exceptions.ProductException;
import com.webnorm.prototypever1.repository.CollectionRepository;
import com.webnorm.prototypever1.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {
    private final ProductRepository productRepository;
    private final CollectionRepository categoryRepository;

    /*
    * [상품 저장]
    * # 상품명 중복검사 <- XXX 안함
    */
    public Product saveProduct(Product product) {
        /*Optional<Product> findProduct = productRepository.findByName(product.getName());
        if (findProduct.isPresent())
            throw new BusinessLogicException(ProductException.PRODUCT_NAME_DUP);*/
        return productRepository.save(product);
    }

    /*
     * [전체 상품 조회]
     */
    /*public Page<Product> findAll() {
        //return productRepository.findAll();
    }*/

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
        Optional<Collection> findCategory = categoryRepository.findByName(category);
        if (findCategory.isEmpty()) throw new BusinessLogicException(CollectionException.COLLECTION_NOT_FOUND);
        return productRepository.findByCategory(category);
    }

    /*
    * [상품 이미지 추가]
    */
    public Product addImageToProduct(String productId, List<Image> imageList) {
        // id 로 product 조회해서 imageList 매핑 후 엔티티 가져오기
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new BusinessLogicException(ProductException.PRODUCT_NOT_FOUND))
                .mapImageList(imageList);
        // db 에 저장
        Product savedProduct = productRepository.save(product);
        return savedProduct;
    }
}
