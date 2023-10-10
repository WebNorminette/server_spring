package com.webnorm.prototypever1.dto.request.order;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.webnorm.prototypever1.dto.request.RequestInterface;
import com.webnorm.prototypever1.entity.member.Address;
import com.webnorm.prototypever1.entity.order.Order;
import com.webnorm.prototypever1.entity.order.OrderInfo;
import lombok.Data;
import lombok.Getter;
import org.springframework.data.annotation.Id;

import java.util.List;

@Data
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class OrderAddRequest implements RequestInterface {
    private String email;

    private Address shippingAddress;    // 배송 주소
    private Address billingAddress;     // 청구지 주소

    private String shippingInfo;    // 배송 방법

    private List<OrderInfo> orderInfoList;  // 상품&개수 정보 리스트
    private int shippingPrice;  // 배송비
    private int totalPrice;     // 총 상품금액
    private int finalPrice;     // 총 결제금액

    @Override
    public Order toEntity() {
        return Order.builder()
                .email(email)
                .shippingAddress(shippingAddress)
                .billingAddress(billingAddress)
                .shippingInfo(shippingInfo)
                .orderInfoList(orderInfoList)
                .shippingPrice(shippingPrice)
                .totalPrice(totalPrice)
                .finalPrice(finalPrice)
                .build();
    }
}
