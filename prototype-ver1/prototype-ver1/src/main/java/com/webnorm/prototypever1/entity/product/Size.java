package com.webnorm.prototypever1.entity.product;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@Document
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Size implements Serializable {
    private String size;
    private int stock;

    @Builder
    public Size(String size, int stock) {
        this.size = size;
        this.stock = stock;
    }
}
