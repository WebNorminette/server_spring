package com.webnorm.prototypever1.entity;


import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

public class Address {
    private ObjectId addressId;
    private String mainAddress;
    private String detailAddress;

    public Address() {
        this.addressId = ObjectId.get();
    }
}
