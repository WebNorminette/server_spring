package com.webnorm.prototypever1.entity.member;


import org.bson.types.ObjectId;

public class Address {
    private ObjectId addressId;
    private String mainAddress;
    private String detailAddress;

    public Address() {
        this.addressId = ObjectId.get();
    }
}
