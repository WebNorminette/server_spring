package com.webnorm.prototypever1.entity.product;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.*;
import org.springframework.data.annotation.Id;

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
    private Collection collection;
    private String shipping;
    private List<Image> imageList;

    public Product mapCategory(Collection collection) {
        this.collection = collection;
        return this;
    }

    public Product mapImageList(List<Image> imageList) {
        this.imageList = imageList;
        return this;
    }
}
