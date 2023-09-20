package com.webnorm.prototypever1.repository;

import com.webnorm.prototypever1.entity.collection.Collection;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface CollectionRepository extends MongoRepository<Collection, String> {
    Optional<Collection> findByName(String name);
}
