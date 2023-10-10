package com.webnorm.prototypever1.entity.product;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.*;
import org.springframework.data.annotation.Id;

@AllArgsConstructor
@Getter
@Builder
public class Collection implements Comparable<Collection>{
    @Id
    private String id;
    private String name;
    private int order;

    public Collection updateName(String name) {
        this.name = name;
        return this;
    }

    public Collection updateOrder(int order) {
        this.order = order;
        return this;
    }

    @Override
    public int compareTo(Collection collection) {
        return this.order - collection.order;
    }
}
