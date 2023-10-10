    package com.webnorm.prototypever1.repository;

import com.webnorm.prototypever1.entity.product.Collection;
import com.webnorm.prototypever1.entity.product.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface ProductRepository extends MongoRepository<Product, String> {
    @Query("{'$or': " +
                "[" +
                    "{'name': {$regex: ?0, $options: 'i'}}, " +
                    "{'color': {$regex: ?0, $options: 'i'}}, " +
                    "{'details': {$regex: ?0, $options: 'i'}}, " +
                    "{'category.name': {$regex: ?0, $options: 'i'}}" +
                "]" +
            "}")
    Page<Product> findByTerm(String term, Pageable pageable);

    //@Query("{'category': {$regex: ?0, $options: 'i'}}")
    Page<Product> findByCollection(Collection collection, Pageable pageable);

    Page<Product> findAll(Pageable pageable);

    List<Product> findByName(String name);

}
