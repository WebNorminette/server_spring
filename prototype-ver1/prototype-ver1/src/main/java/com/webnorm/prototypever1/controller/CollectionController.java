package com.webnorm.prototypever1.controller;

import com.webnorm.prototypever1.dto.request.collection.CollectionAddRequest;
import com.webnorm.prototypever1.dto.request.collection.CollectionUpdateRequest;
import com.webnorm.prototypever1.dto.response.MultiResponse;
import com.webnorm.prototypever1.dto.response.SingleResponse;
import com.webnorm.prototypever1.entity.product.Collection;
import com.webnorm.prototypever1.service.CollectionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/collections")
public class CollectionController {
    private final CollectionService collectionService;

    // 새로운 카테고리(collection) 등록
    @PostMapping
    public SingleResponse addCollection(@RequestBody CollectionAddRequest request) {
        // dto -> entity
        Collection collection = request.toEntity();
        // db 저장
        Collection newCollection = collectionService.saveCollection(collection);
        return new SingleResponse(HttpStatus.OK, "successfully added collection " + newCollection.getName());
    }

    // 카테고리(collection) 목록 전체조회
    @GetMapping
    public MultiResponse searchAllCollections() {
        // db 에서 전체조회
        List<Collection> collectionList = collectionService.findAll();
        return new MultiResponse(HttpStatus.OK, "successfully found all collections", collectionList);
    }

    // 카테고리(collection) 수정
    @PutMapping("/{name}")
    public SingleResponse updateCollections(@PathVariable("name") String name, @RequestBody CollectionUpdateRequest request) {
        // 이름으로 조회 후 수정
        Collection updatedCollection = collectionService.updateCollection(name, request);
        return new SingleResponse(HttpStatus.OK, "successfully updated collection " + updatedCollection.getName());
    }

    // 카테고리(collection) 삭제
    @DeleteMapping("/{name}")
    public SingleResponse deleteCollection(@PathVariable("name") String name) {
        collectionService.deleteCollection(name);
        return new SingleResponse(HttpStatus.OK, "successfully deleted collection " + name);
    }
}
