package com.webnorm.prototypever1.dto.response.product;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.webnorm.prototypever1.entity.product.Image;
import com.webnorm.prototypever1.entity.product.Size;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class ProductListResponse {
    private String id;
    private String name;
    private int price;
    private String color;
    private Image mainImg;
    private Image subImg;
    private int priority;
}
