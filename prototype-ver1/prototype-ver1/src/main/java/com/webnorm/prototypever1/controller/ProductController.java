package com.webnorm.prototypever1.controller;

import com.webnorm.prototypever1.dto.request.product.ProductImgAddRequest;
import com.webnorm.prototypever1.dto.request.product.ProductAddRequest;
import com.webnorm.prototypever1.dto.response.MultiResponse;
import com.webnorm.prototypever1.dto.response.SingleResponse;
import com.webnorm.prototypever1.entity.product.Collection;
import com.webnorm.prototypever1.entity.product.Image;
import com.webnorm.prototypever1.entity.product.Product;
import com.webnorm.prototypever1.service.CollectionService;
import com.webnorm.prototypever1.service.ProductService;
import com.webnorm.prototypever1.service.S3Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
            product.mapCategory(collection);
        }
        Product newProduct = productService.saveProduct(product);     // db 저장
        return new SingleResponse(HttpStatus.OK, "successfully added Product " + newProduct.getName());
    }

    // 전체 카테고리에 해당하는 상품 조회(ALL)
    @GetMapping("/collections/all")
    public MultiResponse mainPageProducts() {
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

    //
    @GetMapping("/file/{filename}")
    public SingleResponse downloadFile(@PathVariable("filename") String filename) {
        URL url = s3Service.findFile(filename);
        return new SingleResponse(HttpStatus.OK, "ok", url);
    }
}
