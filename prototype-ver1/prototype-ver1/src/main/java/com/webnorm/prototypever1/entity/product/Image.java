package com.webnorm.prototypever1.entity.product;

import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document
@Getter
@Builder
@AllArgsConstructor
public class Image {
    private String productId;
    private String originName;
    private String saveName;
    private String url;

    public Image mapProductId(String productId) {
        this.productId = productId;
        return this;
    }
}
