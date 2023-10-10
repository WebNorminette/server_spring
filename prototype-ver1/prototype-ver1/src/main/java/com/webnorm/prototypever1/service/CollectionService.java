package com.webnorm.prototypever1.service;

import com.webnorm.prototypever1.dto.request.collection.CollectionUpdateRequest;
import com.webnorm.prototypever1.entity.product.Collection;
import com.webnorm.prototypever1.exception.exceptions.BusinessLogicException;
import com.webnorm.prototypever1.exception.exceptions.CollectionException;
import com.webnorm.prototypever1.repository.CollectionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class CollectionService {
    private final CollectionRepository collectionRepository;

    /*
     * [카테고리 생성]
     * # 카테고리 이름 중복체크
     */
    public Collection saveCollection(Collection collection) {
        if (collection.getName().isEmpty())
            throw new BusinessLogicException(CollectionException.INVALID_COLLECTION_NAME);
        Optional<Collection> findCategory = collectionRepository.findByName(collection.getName());
        if (findCategory.isPresent()) throw new BusinessLogicException(CollectionException.COLLECTION_NAME_DUP);
        return collectionRepository.save(collection);
    }

    /*
     * [카테고리 이름으로 조회]
     */
    public Collection findByName(String name) {
        Optional<Collection> findCategory = collectionRepository.findByName(name);
        if (findCategory.isEmpty()) throw new BusinessLogicException(CollectionException.COLLECTION_NOT_FOUND);
        return findCategory.get();
    }

    /*
     * [카테고리 전체 조회]
     */
    public List<Collection> findAll() {
        List<Collection> collectionList = collectionRepository.findAll();
        Collections.sort(collectionList);
        return collectionList;
    }

    /*
     * [카테고리 수정]
     */
    public Collection updateCollection(String name, CollectionUpdateRequest request) {
        Collection collection = collectionRepository.findByName(name)
                .orElseThrow(() -> new BusinessLogicException(CollectionException.COLLECTION_NOT_FOUND))
                .updateName(request.getName())
                .updateOrder(request.getOrder());
        return collectionRepository.save(collection);
    }

    /*
     * [카테고리 삭제]
     */
    public void deleteCollection(String name) {
        Collection findCollection = collectionRepository.findByName(name)
                .orElseThrow(() -> new BusinessLogicException(CollectionException.COLLECTION_NOT_FOUND));
        collectionRepository.delete(findCollection);
    }
}
