package com.webnorm.prototypever1.entity.product;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.webnorm.prototypever1.entity.category.Category;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Getter
@Builder
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
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
    private List<Image> imageList;

    public void specifyCategory(Category category) {
        this.category = category;
    }
}
