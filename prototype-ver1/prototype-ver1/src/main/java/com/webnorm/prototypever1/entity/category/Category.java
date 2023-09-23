package com.webnorm.prototypever1.entity.category;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Category {
    @Id
    private String id;
    private String name;

    @Builder
    public Category(String name) {
        this.name = name;
    }
}
