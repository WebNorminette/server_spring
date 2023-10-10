package com.webnorm.prototypever1.controller;

import com.webnorm.prototypever1.dto.request.product.ProductImgAddRequest;
import com.webnorm.prototypever1.dto.request.product.ProductAddRequest;
import com.webnorm.prototypever1.dto.request.product.ProductUpdateRequest;
import com.webnorm.prototypever1.dto.response.MultiResponse;
import com.webnorm.prototypever1.dto.response.SingleResponse;
import com.webnorm.prototypever1.dto.response.product.ProductByIdResponse;
import com.webnorm.prototypever1.dto.response.product.ProductListResponse;
import com.webnorm.prototypever1.entity.product.SimpleProduct;
import com.webnorm.prototypever1.entity.product.Collection;
import com.webnorm.prototypever1.entity.product.Image;
import com.webnorm.prototypever1.entity.product.Product;
import com.webnorm.prototypever1.service.CollectionService;
import com.webnorm.prototypever1.service.ProductService;
import com.webnorm.prototypever1.service.S3Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.net.URL;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;
    private final CollectionService categoryService;
    private final S3Service s3Service;

    // 상품 추가
    @PostMapping("/collections/{collectionName}/products")
    public SingleResponse addProduct(
            @PathVariable("collectionName") String collectionName,
            @RequestBody ProductAddRequest request
    ) {
        // dto -> entity 전환
        Product product = request.toEntity();
        if (collectionName != null) {  // 카테고리 설정 : 카테고리가 존재하는 경우에만 설정
            Collection collection = categoryService.findByName(collectionName);
            product.mapCategory(collectionName);
        }
        Product newProduct = productService.saveProduct(product);     // db 저장
        return new SingleResponse(HttpStatus.OK, "successfully added Product " + newProduct.getName());
    }

    /*// 모든 카테고리의 상품 조회(ALL)    -> 카테고리 이름으로 조회 api 에 통합
    @GetMapping("/collections/all")
    public MultiResponse mainPageProducts(Pageable pageable) {
        Sort sort = Sort.by("priority").ascending();    // 정렬기준 설정
        Page<ProductListResponse> productList = productService
                .findAll(PageRequest.of(pageable.getPageNumber(), 2, sort))
                .map(p -> p.toListResponse());// paging + sort
        return new MultiResponse(HttpStatus.OK, "successfully found all Products", productList);
    }*/

    // 키워드로 상품 조회
    @GetMapping("/pages/search")
    public MultiResponse searchProductsByTerm(@RequestParam("term") String term, Pageable pageable) {
        Sort sort = Sort.by("priority").ascending();    // 정렬기준 설정
        Page<ProductListResponse> productList = productService.findByTerm(term, PageRequest.of(pageable.getPageNumber(), 2, sort))
                .map(p -> p.toListResponse());
        return new MultiResponse(HttpStatus.OK, "successfully found Products contain " + term, productList);
    }

    // 카테고리로 상품 조회
    @GetMapping("/collections/{collectionName}")
    public MultiResponse searchProductByCategory(@PathVariable("collectionName") String collectionName, Pageable pageable) {
        Sort sort = Sort.by("priority").ascending();    // 정렬기준 설정
        Page<ProductListResponse> productList = null;
        if (collectionName.equals("all"))     // 전체 카테고리 조회시
            productList = productService.findAll(PageRequest.of(pageable.getPageNumber(), 2, sort))
                    .map(p -> p.toListResponse());  // paging + sort
        else // 카테고리 이름으로 조회시
            productList = productService.findByCollection(collectionName, PageRequest.of(pageable.getPageNumber(), 2, sort))
                    .map((p -> p.toListResponse()));    // paging + sort
        return new MultiResponse(HttpStatus.OK, "successfully found Products by category " + collectionName, productList);
    }

    // 상품 상세페이지(단건조회)
    @GetMapping("/collections/{collectionName}/products/{productId}")
    public SingleResponse searchProductById(@PathVariable("productId") String productId) {
        Product product = productService.findById(productId);   // 상품 조회
        List<SimpleProduct> otherColors = productService.findOtherColors(productId);  // 다른 색상 조회
        ProductByIdResponse productByIdResponse = product.toSingleResponse(otherColors);
        return new SingleResponse(HttpStatus.OK, "successfully found Product by Id ", productByIdResponse);
    }

    // 사진파일 업로드 -> 상품에 사진 추가
    @PostMapping("/collections/{collectionName}/products/img")
    public SingleResponse addProductImg(@ModelAttribute ProductImgAddRequest request) {
        // S3 에 사진 저장 후 Image 엔티티 생성
        List<Image> imageList = request.getMultipartFileList().stream()
                .map(file -> s3Service.saveFile(file).mapProductId(request.getProductId())).toList();
        // id로 product 찾아서 imageList 저장
        Product product = productService.addImageToProduct(request.getProductId(), imageList);
        // url 리스트 생성
        List<String> urlList = imageList.stream().map(i -> i.getUrl()).toList();
        //log.info(request.getProductId());
        return new SingleResponse(HttpStatus.OK, "ok", urlList);
    }

    // 파일 조회 (미사용)
    @GetMapping("/file/{filename}")
    public SingleResponse downloadFile(@PathVariable("filename") String filename) {
        URL url = s3Service.findFile(filename);
        return new SingleResponse(HttpStatus.OK, "ok", url);
    }

    // 상품 수정
    @PutMapping("/collections/{collectionName}/products/{productId}")
    public SingleResponse updateProduct(@PathVariable("productId") String productId,
                                        @RequestBody ProductUpdateRequest request) {
        // 상품 수정
        Product updatedProduct = productService.updateProduct(productId, request);
        return new SingleResponse(HttpStatus.OK, "successfully updated product", updatedProduct);
    }

    // 상품 삭제
    @DeleteMapping("/collections/{collectionName}/products/{productId}")
    public SingleResponse deleteProduct(@PathVariable("productId") String productId) {
        // 삭제
        Product product = productService.deleteProduct(productId);
        return new SingleResponse(HttpStatus.OK, "successfully deleted product", product);
    }
}
