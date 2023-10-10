package com.webnorm.prototypever1.repository;

import com.webnorm.prototypever1.entity.order.Order;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends MongoRepository<Order, String>, PagingAndSortingRepository<Order, String> {

    public Page<Order> findByEmail(String email, Pageable pageable);
}
