package com.webnorm.prototypever1.dto.request.product;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.util.List;

@Data
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class ProductImgAddRequest implements Serializable {
    private String productId;
    private List<MultipartFile> multipartFileList;
}
