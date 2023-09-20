package com.webnorm.prototypever1.entity.collection;


import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Collection {
    @Id
    private String id;

    private String name;
    private int price;
    private List<Color> colors;
    private String details;

    @Builder
    public Collection(String name, int price, List<Color> colors,
                      String details) {
        this.name = name;
        this.price = price;
        this.colors = colors;
        this.details = details;
    }
}
