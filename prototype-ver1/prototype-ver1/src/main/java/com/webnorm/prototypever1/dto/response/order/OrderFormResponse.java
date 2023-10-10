package com.webnorm.prototypever1.dto.response.order;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.webnorm.prototypever1.entity.member.Address;
import com.webnorm.prototypever1.entity.order.OrderInfo;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.util.List;

@Data
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class OrderFormResponse {
    @Id
    private String id;

    private String email;

    private List<Address> addressList;    // 주소 리스트
    private Address billingAddress;     // 청구지 주소

    private List<OrderInfo> orderInfoList;  // 상품&개수 정보 리스트
    private int totalPrice;     // 총 상품금액
    private int shippingPrice;  // 배송비
    private int finalPrice;     // 총 결제금액

    @Builder
    public OrderFormResponse(String email, List<Address> addressList,
                             Address billingAddress, List<OrderInfo> orderInfoList) {
        this.email = email;
        this.addressList = addressList;
        this.billingAddress = billingAddress;
        this.orderInfoList = orderInfoList;
        // 총 상품금액 계산
        int totalPrice = 0;
        for (OrderInfo orderInfo : orderInfoList) {
            totalPrice += orderInfo.getPrice();
        }
        this.totalPrice = totalPrice;
        // 배송비 계산
        if (totalPrice >= 70000)    this.shippingPrice = 0;
        else    this.shippingPrice = 2500;
        // 총 결제금액 계산
        this.finalPrice = totalPrice + shippingPrice;
    }
}
