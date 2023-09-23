package com.webnorm.prototypever1.entity.product;


import com.webnorm.prototypever1.entity.category.Category;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product {
    @Id
    private String id;

    private String name;
    private int price;
    private List<Color> colors;
    private String details;
    private Category category;
    private String shipping;

    @Builder
    public Product(String name, int price, List<Color> colors,
                   String details, String shipping) {
        this.name = name;
        this.price = price;
        this.colors = colors;
        this.details = details;
        this.shipping = shipping;
    }

    public void specifyCategory(Category category) {
        this.category = category;
    }
}
