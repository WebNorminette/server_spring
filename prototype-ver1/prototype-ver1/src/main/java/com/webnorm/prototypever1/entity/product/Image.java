package com.webnorm.prototypever1.entity.product;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document
@Getter
@Builder
@AllArgsConstructor
public class Image {
    private String id;
    private String productId;
    private String originName;
    private String saveName;
}
