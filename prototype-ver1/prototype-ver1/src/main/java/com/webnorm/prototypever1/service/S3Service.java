package com.webnorm.prototypever1.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.webnorm.prototypever1.entity.product.Image;
import com.webnorm.prototypever1.exception.exceptions.BusinessLogicException;
import com.webnorm.prototypever1.exception.exceptions.ProductException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3Service {
    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    /*
     * [파일 저장 메서드]
     */
    public Image saveFile(MultipartFile multipartFile) {

        // 원본 파일명 가져오기
        String originalFilename = multipartFile.getOriginalFilename();
        // 저장할 파일명 만들기(uuid 사용)
        String saveFilename = UUID.randomUUID() + originalFilename;

        // 파일 메타데이타 가져오기
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(multipartFile.getSize());
        metadata.setContentType(multipartFile.getContentType());

        // S3 에 저장
        try {
            amazonS3.putObject(bucket, saveFilename, multipartFile.getInputStream(), metadata);
        } catch (IOException e) {
            throw new BusinessLogicException(ProductException.FILE_INPUT_ERROR);
        }
        // 저장한 파일 url 가져오기
        String url = amazonS3.getUrl(bucket, saveFilename).toString();

        return Image.builder()
                .originName(originalFilename)
                .saveName(saveFilename)
                .metadata(metadata)
                .url(url)
                .build();
    }

    /*
     * [파일 조회 메서드]
     */
    public URL findFile(String filename) {
        URL url = amazonS3.getUrl(bucket, filename);
        return url;
    }
}
