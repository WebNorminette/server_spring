package com.webnorm.prototypever1.controller;

import com.webnorm.prototypever1.api.request.AddCollectionsRequest;
import com.webnorm.prototypever1.api.response.SingleResponse;
import com.webnorm.prototypever1.entity.collection.Collection;
import com.webnorm.prototypever1.service.CollectionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/collections")
public class CollectionController {
    private final CollectionService collectionService;

    // 상품 추가
    @PostMapping
    public SingleResponse addCollection(@RequestBody AddCollectionsRequest request) {
        Collection collection = Collection.builder()
                .name(request.getName())
                .price(request.getPrice())
                .colors(request.getColorList())
                .details(request.getDetails())
                .build();
        Collection newCollection = collectionService.createCollection(collection);
        return new SingleResponse(HttpStatus.OK, "successfully added Collection", newCollection);
    }
}
