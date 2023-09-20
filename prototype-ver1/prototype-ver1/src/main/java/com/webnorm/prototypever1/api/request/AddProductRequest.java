package com.webnorm.prototypever1.api.request;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.webnorm.prototypever1.entity.category.Category;
import com.webnorm.prototypever1.entity.product.Color;
import lombok.Data;

import java.util.List;

@Data
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class AddProductRequest {
    private String name;
    private int price;
    private List<Color> colorList;
    private String details;
    private String category;
    private String shipping;
}
