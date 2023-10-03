package com.webnorm.prototypever1.entity.member;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;


@Getter
@Document
public class Address implements Comparable<Address> {

    private String id;                  // id
    private String name;                // 주소 이름
    private String address;             // 기본 주소
    private String postCode;            // 우편번호
    private String detailAddress;       // 상세주소
    private String extraAddress;        // 참고항목
    // 등록 시간
    private LocalDateTime createTime = LocalDateTime.now();

    @Builder
    public Address(String name, String address, String postCode, String detailAddress, String extraAddress) {
        this.id = ObjectId.get().toString();
        this.name = name;
        this.address = address;
        this.postCode = postCode;
        this.detailAddress = detailAddress;
        this.extraAddress = extraAddress;
    }

    // 정렬 기준
    @Override
    public int compareTo(Address address) {
        return this.createTime.compareTo(address.createTime);
    }

    // 정보 수정
    public Address update(Address address) {
        if (address.getName() != null) this.name = address.getName();
        if (address.getAddress() != null) this.address = address.getAddress();
        if (address.getPostCode() != null) this.postCode = address.getPostCode();
        if (address.getDetailAddress() != null) this.detailAddress = address.getDetailAddress();
        if (address.getExtraAddress() != null) this.extraAddress = address.getExtraAddress();
        return this;
    }
}
