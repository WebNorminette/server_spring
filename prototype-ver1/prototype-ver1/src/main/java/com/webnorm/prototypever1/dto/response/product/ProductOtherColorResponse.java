package com.webnorm.prototypever1.dto.response.product;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.webnorm.prototypever1.entity.product.Image;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class ProductOtherColorResponse {
    private String id;
    private String color;
    private Image mainImg;
}
