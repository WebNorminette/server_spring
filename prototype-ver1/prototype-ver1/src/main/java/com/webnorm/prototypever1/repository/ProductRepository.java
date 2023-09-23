package com.webnorm.prototypever1.repository;

import com.webnorm.prototypever1.entity.product.Product;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface ProductRepository extends MongoRepository<Product, String> {
    Optional<Product> findByName(String name);

    @Query("{'$or': " +
                "[" +
                    "{'name': {$regex: ?0, $options: 'i'}}, " +
                    "{'color': {$regex: ?0, $options: 'i'}}, " +
                    "{'details': {$regex: ?0, $options: 'i'}}, " +
                    "{'category.name': {$regex: ?0, $options: 'i'}}" +
                "]" +
            "}")
    List<Product> findByTerm(String term);

    @Query("{'category.name': {$regex: ?0, $options: 'i'}}")
    List<Product> findByCategory(String category);
}
