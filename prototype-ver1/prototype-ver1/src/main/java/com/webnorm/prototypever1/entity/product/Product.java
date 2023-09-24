package com.webnorm.prototypever1.entity.product;


import com.webnorm.prototypever1.entity.category.Category;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product {
    @Id
    private String id;

    private String name;
    private int price;
    private String color;
    private List<Size> sizeList;
    private String details;
    private Category category;
    private String shipping;
    private Image image;

    @Builder
    public Product(String name, int price, String color,
                   List<Size> sizeList, String details, Category category,
                   String shipping, Image image) {
        this.name = name;
        this.price = price;
        this.color = color;
        this.sizeList = sizeList;
        this.details = details;
        this.category = category;
        this.shipping = shipping;
        this.image = image;
    }

    public void specifyCategory(Category category) {
        this.category = category;
    }
}
