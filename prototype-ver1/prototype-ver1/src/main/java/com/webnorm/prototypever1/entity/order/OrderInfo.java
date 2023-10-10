package com.webnorm.prototypever1.entity.order;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.webnorm.prototypever1.entity.product.Product;
import com.webnorm.prototypever1.entity.product.SimpleProduct;
import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class OrderInfo {
    @NonNull
    private SimpleProduct product;  // id, name, price, color, mainImg
    private int count;
    private int price;      // 상품가격 * 개수

    @Builder
    public OrderInfo(@NonNull SimpleProduct product, int count) {
        this.product = product;
        this.count = count;
        this.price = product.getPrice() * count;
    }
}
