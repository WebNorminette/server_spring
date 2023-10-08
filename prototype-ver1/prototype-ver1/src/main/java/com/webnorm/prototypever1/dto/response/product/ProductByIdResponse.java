package com.webnorm.prototypever1.dto.response.product;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.webnorm.prototypever1.entity.product.Image;
import com.webnorm.prototypever1.entity.product.Size;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class ProductByIdResponse {
    private String id;
    private String name;
    private int price;
    private String color;
    private List<ProductOtherColorResponse> otherColors;
    private List<Size> sizeList;
    private String details;
    private String collection;
    private String shipping;
    private List<Image> imageList;
    private int priority;
}
