package com.webnorm.prototypever1.entity.collection;

import lombok.*;

import java.util.List;

@Data
public class Color {
    private String color;
    private List<Size> sizeList;

    public Color(String color, List<Size> sizeList) {
        this.color = color;
        this.sizeList = sizeList;
    }
}
