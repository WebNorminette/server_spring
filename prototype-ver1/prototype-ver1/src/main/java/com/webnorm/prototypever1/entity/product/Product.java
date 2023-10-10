package com.webnorm.prototypever1.entity.product;


import com.webnorm.prototypever1.dto.response.product.ProductByIdResponse;
import com.webnorm.prototypever1.dto.response.product.ProductListResponse;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Document
public class Product {
    @Id
    private String id;

    private String name;
    private int price;
    private String color;
    private List<Size> sizeList;
    private String details;
    private String collection;
    private String shipping;
    private List<Image> imageList;
    private int priority;
    private LocalDateTime createDate;

    @Builder
    public Product(String name, int price, String color,
                   List<Size> sizeList, String details, String collection,
                   String shipping, List<Image> imageList, int priority) {
        this.name = name;
        this.price = price;
        this.color = color;
        this.sizeList = sizeList;
        this.details = details;
        this.collection = collection;
        this.shipping = shipping;
        this.imageList = imageList;
        this.priority = priority;
        this.createDate = LocalDateTime.now();
    }

    public Product mapCategory(String collection) {
        this.collection = collection;
        return this;
    }

    public Product mapImageList(List<Image> imageList) {
        this.imageList = imageList;
        return this;
    }

    public ProductListResponse toListResponse() {
        Image mainImg = null;
        Image subImg = null;
        if (imageList != null) {
            mainImg = imageList.get(0);
            if (imageList.size() > 1)
                subImg = imageList.get(1);
        }
        return ProductListResponse.builder()
                .id(id)
                .name(name)
                .price(price)
                .color(color)
                .mainImg(mainImg)
                .subImg(subImg)
                .priority(priority)
                .createDate(createDate)
                .build();
    }

    public ProductByIdResponse toSingleResponse(List<SimpleProduct> otherColors) {
        return ProductByIdResponse.builder()
                .id(id)
                .name(name)
                .price(price)
                .color(color)
                .otherColors(otherColors)
                .sizeList(sizeList)
                .details(details)
                .collection(collection)
                .shipping(shipping)
                .imageList(imageList)
                .priority(priority)
                .createDate(createDate)
                .build();
    }

    public Product update(String name, int price, String color,
                          String details, String collection, String shipping,
                          List<Size> sizeList, int priority) {
        this.name = name;
        this.price = price;
        this.color = color;
        this.details = details;
        this.collection = collection;
        this.sizeList = sizeList;
        this.shipping = shipping;
        this.priority = priority;
        return this;
    }

    public SimpleProduct toSimpleProduct() {
        Image mainImg = null;
        if (imageList != null && imageList.size() > 0) mainImg = imageList.get(0);
        return SimpleProduct.builder()
                .name(name)
                .price(price)
                .color(color)
                .mainImg(mainImg)
                .build();
    }
}
