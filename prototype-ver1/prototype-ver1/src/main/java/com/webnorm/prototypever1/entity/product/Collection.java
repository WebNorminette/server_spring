package com.webnorm.prototypever1.entity.product;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.*;
import org.springframework.data.annotation.Id;

@AllArgsConstructor
@Getter
@Builder
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class Collection {
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
}
