package com.webnorm.prototypever1.service;

import com.webnorm.prototypever1.entity.collection.Collection;
import com.webnorm.prototypever1.exception.exceptions.BusinessLogicException;
import com.webnorm.prototypever1.exception.exceptions.CollectionException;
import com.webnorm.prototypever1.repository.CollectionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CollectionService {
    private final CollectionRepository collectionRepository;

    /*
    * [상품 저장]
    * # 상품명 중복검사 */
    public Collection createCollection(Collection collection) {
        Optional<Collection> findCollection = collectionRepository.findByName(collection.getName());
        if (findCollection.isPresent())
            throw new BusinessLogicException(CollectionException.COLLECTION_NAME_DUP);
        return collectionRepository.save(collection);
    }
}
