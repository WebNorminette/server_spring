package com.webnorm.prototypever1.entity.collection;

import lombok.*;

@Data
public class Size {
    private String size;
    private int stock;

    @Builder
    public Size(String size, int stock) {
        this.size = size;
        this.stock = stock;
    }
}
