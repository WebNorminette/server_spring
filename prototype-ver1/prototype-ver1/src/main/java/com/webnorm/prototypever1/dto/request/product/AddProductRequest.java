package com.webnorm.prototypever1.dto.request.product;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.webnorm.prototypever1.dto.request.RequestInterface;
import com.webnorm.prototypever1.entity.product.Product;
import com.webnorm.prototypever1.entity.product.Size;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class AddProductRequest implements RequestInterface {
    private String name;
    private int price;
    private List<Size> sizeList;
    private String details;
    private String category;
    private String shipping;

    @Override
    public Product toEntity() {
        return Product.builder()
                .name(name)
                .price(price)
                .sizeList(sizeList)
                .details(details)
                .shipping(shipping)
                .build();
    }
}
