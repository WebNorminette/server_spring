package com.webnorm.prototypever1.dto.request.collection;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.webnorm.prototypever1.dto.request.RequestInterface;
import com.webnorm.prototypever1.entity.product.Collection;
import lombok.Data;

@Data
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class CollectionAddRequest implements RequestInterface {
    private String name;
    private int order;

    public Collection toEntity() {
        return Collection.builder()
                .name(name)
                .order(order)
                .build();
    }
}
