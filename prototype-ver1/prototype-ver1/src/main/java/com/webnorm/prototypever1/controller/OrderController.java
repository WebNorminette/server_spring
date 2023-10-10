package com.webnorm.prototypever1.controller;

import com.webnorm.prototypever1.dto.request.order.OrderAddRequest;
import com.webnorm.prototypever1.dto.response.MultiResponse;
import com.webnorm.prototypever1.dto.response.SingleResponse;
import com.webnorm.prototypever1.dto.response.order.OrderFormResponse;
import com.webnorm.prototypever1.entity.member.Member;
import com.webnorm.prototypever1.entity.order.Order;
import com.webnorm.prototypever1.entity.order.OrderInfo;
import com.webnorm.prototypever1.entity.product.Product;
import com.webnorm.prototypever1.entity.product.SimpleProduct;
import com.webnorm.prototypever1.service.MemberService;
import com.webnorm.prototypever1.service.OrderService;
import com.webnorm.prototypever1.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;
    private final MemberService memberService;
    private final ProductService productService;

    // 주문 페이지 로드
    @GetMapping("/direct/{productId}/{count}")
    public SingleResponse directOrderForm(@AuthenticationPrincipal User user,
                                          @PathVariable("productId") String productId,
                                          @PathVariable("count") int count) {
        // 회원 조회
        Member member = memberService.findMemberByEmail(user.getUsername());
        // 상품 조회
        SimpleProduct product = productService.findById(productId).toSimpleProduct();
        // 주문정보 생성
        OrderInfo orderInfo = OrderInfo.builder()
                .product(product)
                .count(count)
                .build();
        // 주문정보 리스트 생성
        List<OrderInfo> orderInfoList = new ArrayList<>();
        orderInfoList.add(orderInfo);   // 주문정보 추가
        // 결제 폼 생성
        OrderFormResponse orderFormResponse = OrderFormResponse.builder()
                .email(member.getEmail())
                .addressList(member.getAddressList())
                .billingAddress(member.getDefaultAddress())
                .orderInfoList(orderInfoList)
                .build();
        return new SingleResponse(HttpStatus.OK, "successfully make orderForm", orderFormResponse);
    }

    // 주문 생성(실제 주문 실행)
    @PostMapping
    public SingleResponse makeOrder(@RequestBody OrderAddRequest request) {
        Order order = request.toEntity();   // dto -> entity
        Order savedOrder = orderService.saveOrder(order);   // 주문 저장
        return new SingleResponse(HttpStatus.OK, "successfully saved order", savedOrder.getId());
    }

    // 주문 조회(관리자용)
    @GetMapping
    public MultiResponse orderList(Pageable pageable) {
        Sort sort = Sort.by("createDate").descending();
        Page<Order> orderList = orderService.findAll(PageRequest.of(pageable.getPageNumber(), 2, sort));
        return new MultiResponse(HttpStatus.OK, "successfully found all orders", orderList);
    }

    // 주문 취소(삭제)
    @DeleteMapping("/{orderId}")
    public SingleResponse cancelOrder(@PathVariable("orderId") String orderId) {
        Order deletedOrder = orderService.delete(orderId);
        return new SingleResponse(HttpStatus.OK, "successfully deleted order", deletedOrder.getId());
    }

}