package com.webnorm.prototypever1.service;

import com.webnorm.prototypever1.dto.response.order.OrderFormResponse;
import com.webnorm.prototypever1.entity.order.Order;
import com.webnorm.prototypever1.exception.exceptions.BusinessLogicException;
import com.webnorm.prototypever1.exception.exceptions.OrderException;
import com.webnorm.prototypever1.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;

    /*
    * [주문 저장]
    * */
    public Order saveOrder(Order order) {
        return orderRepository.save(order);
    }

    /*
     * [회원 주문 조회]
     * */
    public Page<Order> findByEmail(String email, Pageable pageable) {
        return orderRepository.findByEmail(email, pageable);
    }


    /*
     * [전체 주문 조회]
     * */
    public Page<Order> findAll(Pageable pageable) {
        return orderRepository.findAll(pageable);
    }

    /*
     * [주문 삭제]
     * */
    public Order delete(String orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new BusinessLogicException(OrderException.ORDER_NOT_FOUND));
        orderRepository.delete(order);
        return order;
    }

}
