package com.webnorm.prototypever1.service;

import com.webnorm.prototypever1.dto.request.product.ProductUpdateRequest;
import com.webnorm.prototypever1.entity.product.SimpleProduct;
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

import java.util.ArrayList;
import java.util.List;

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
     * [id 로 조회]
     * */
    public Product findById(String productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new BusinessLogicException(ProductException.PRODUCT_NOT_FOUND));
    }

    /*
     * [같은 상품의 다른 색상 조회]
     * */
    public List<SimpleProduct> findOtherColors(String productId) {
        // id 로 조회
        Product productById = productRepository.findById(productId)
                .orElseThrow(() -> new BusinessLogicException(ProductException.PRODUCT_NOT_FOUND));
        // 동일한 상품명 조회
        List<Product> productListByName = productRepository.findByName(productById.getName());
        // id 에 해당하는 상품을 제외한 나머지 상품 id 리스트 리턴
        List<SimpleProduct> productList = new ArrayList<>();
        for (Product product : productListByName) {
            if (!product.getId().equals(productId)) {
                Image mainImg = null;
                if (product.getImageList() != null) mainImg = product.getImageList().get(0);
                productList.add(SimpleProduct.builder()
                        .id(product.getId())
                        .color(product.getColor())
                        .mainImg(mainImg)
                        .build());
            }
        }
        return productList;
    }

    /*
     * [전체 상품 조회]
     */
    public Page<Product> findAll(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    /*
    * [키워드로 조회]
    */
    public Page<Product> findByTerm(String term, Pageable pageable) {
        Page<Product> products = productRepository.findByTerm(term, pageable);
        return products;
    }

    /*
     * [카테고리로 조회]
     */
    public Page<Product> findByCollection(String collection, Pageable pageable) {
        // 카테고리 존재 여부 확인
        Collection findCollection = categoryRepository.findByName(collection)
                .orElseThrow(() -> new BusinessLogicException(CollectionException.COLLECTION_NOT_FOUND));
        return productRepository.findByCollection(findCollection, pageable);
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

    /*
     * 상품 수정
     * */
    public Product updateProduct(String productId, ProductUpdateRequest request) {
        // id 로 상품 조회
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new BusinessLogicException(ProductException.PRODUCT_NOT_FOUND));
        // 수정
        Product updatedProduct = product.update(request.getName(),
                request.getPrice(),
                request.getColor(),
                request.getDetails(),
                request.getCollection(),
                request.getShipping(),
                request.getSizeList(),
                request.getPriority());
        // 수정된 엔티티 저장
        return productRepository.save(updatedProduct);
    }

    /*
     * [상품 삭제]
     * */
    public Product deleteProduct(String productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new BusinessLogicException(ProductException.PRODUCT_NOT_FOUND));
        productRepository.deleteById(productId);
        return product;
    }
}
