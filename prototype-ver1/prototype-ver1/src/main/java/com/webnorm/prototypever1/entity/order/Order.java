package com.webnorm.prototypever1.entity.order;

import com.webnorm.prototypever1.entity.member.Address;
import com.webnorm.prototypever1.entity.member.Member;
import com.webnorm.prototypever1.entity.product.SimpleProduct;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Document
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    @Id
    private String id;

    private String email;

    private Address shippingAddress;    // 배송 주소
    private Address billingAddress;     // 청구지 주소

    private String shippingInfo;    // 배송 방법

    private List<OrderInfo> orderInfoList;  // 상품&개수 정보 리스트
    private int shippingPrice;  // 배송비
    private int totalPrice;     // 총 상품금액
    private int finalPrice;     // 총 결제금액

    private LocalDateTime createDate;   // 주문시각

    @Builder
    public Order(String email, Address shippingAddress, Address billingAddress,
                 String shippingInfo, List<OrderInfo> orderInfoList, int shippingPrice,
                 int totalPrice, int finalPrice) {
        this.email = email;
        this.shippingAddress = shippingAddress;
        this.billingAddress = billingAddress;
        this.shippingInfo = shippingInfo;
        this.orderInfoList = orderInfoList;
        this.shippingPrice = shippingPrice;
        this.totalPrice = totalPrice;
        this.finalPrice = finalPrice;
        this.createDate = LocalDateTime.now();
    }
}
