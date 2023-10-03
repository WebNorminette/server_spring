package com.webnorm.prototypever1.dto.request.collection;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.Data;

@Data
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class CollectionUpdateRequest {
    private String name;
    private int order;
}
