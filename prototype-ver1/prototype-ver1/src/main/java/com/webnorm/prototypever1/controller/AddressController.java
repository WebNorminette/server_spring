package com.webnorm.prototypever1.controller;

import com.webnorm.prototypever1.dto.request.address.AddressAddRequest;
import com.webnorm.prototypever1.dto.request.address.AddressUpdateRequest;
import com.webnorm.prototypever1.dto.response.AddressListResponse;
import com.webnorm.prototypever1.dto.response.MultiResponse;
import com.webnorm.prototypever1.dto.response.SingleResponse;
import com.webnorm.prototypever1.entity.member.Address;
import com.webnorm.prototypever1.entity.member.Member;
import com.webnorm.prototypever1.exception.exceptions.BusinessLogicException;
import com.webnorm.prototypever1.exception.exceptions.MemberException;
import com.webnorm.prototypever1.service.AddressService;
import com.webnorm.prototypever1.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/addresses")
public class AddressController {
    private final MemberService memberService;
    private final AddressService addressService;

    // 주소 추가
    @PostMapping
    public SingleResponse addAddress(
            @AuthenticationPrincipal User user, // 현재 로그인한 사용자 불러오기
            @RequestBody AddressAddRequest request) {
        // dto -> entity
        Address address = request.toEntity();
        // 현재 로그인한 회원 찾아서 주소 추가
        memberService.addAddress(user.getUsername(), address);
        // 기본 배송지로 요청한 경우 설정
        if (request.is_default())
            memberService.setDefaultAddress(user.getUsername(), address.getId());
        /*// 주소 저장
        Address savedAddress = addressService.createAddress(address);*/
        return new SingleResponse(HttpStatus.OK, "successfully saved address");
    }

    // 기본 배송지 설정
    @PostMapping("/defaultAddress/{addressId}")
    public SingleResponse setDefaultAddress(
            @AuthenticationPrincipal User user,
            @PathVariable("addressId") String addressId) {
        /*// id 로 주소 조회
        Address newDefaultAddress = addressService.findById(addressId);*/
        // 현재 로그인한 회원의 기존 기본 배송지 해제 + 새 기본 배송지 설정
        memberService.setDefaultAddress(user.getUsername(), addressId);
        return new SingleResponse(HttpStatus.OK, "successfully set default address ");
    }

    /*// 기본 배송지 조회
    @GetMapping("/defaultAddress")
    public SingleResponse findDefaultAddress(@AuthenticationPrincipal User user) {
        return new SingleResponse(HttpStatus.OK, "successfully found default address", defaultAddress);
    }*/

    // 주소 리스트 조회
    @GetMapping
    public SingleResponse addressList(@AuthenticationPrincipal User user) {
        Address defaultAddress = memberService.findDefaultAddress(user.getUsername());
        List<Address> addressList = memberService.findAllAddress(user.getUsername());
        List<Address> resultAddressList = new ArrayList<>();
        for (Address address : addressList) {
            if (!address.getId().equals(defaultAddress.getId()))
                resultAddressList.add(address);
        }
        AddressListResponse response = AddressListResponse.builder()
                .defaultAddress(defaultAddress)
                .addressList(resultAddressList)
                .build();
        return new SingleResponse(HttpStatus.OK, "successfully found address list", response);
    }

    // 주소 수정
    @PutMapping("/{addressId}")
    public SingleResponse updateAddress(
            @AuthenticationPrincipal User user,
            @PathVariable("addressId") String addressId,
            @RequestBody AddressUpdateRequest request
    ) {
        // id 로 주소 찾아서 수정
        Member updatedMember = memberService.updateAddress(user.getUsername(), addressId, request);
        // 수정된 주소 회원정보에 저장
        return new SingleResponse(HttpStatus.OK, "successfully updated address");
    }

    // 주소 삭제
    @DeleteMapping("/{addressId}")
    public SingleResponse deleteAddress(
            @AuthenticationPrincipal User user,
            @PathVariable("addressId") String addressId
    ) {
        // id 로 주소 찾아서 삭제
        memberService.deleteAddress(user.getUsername(), addressId);
        return new SingleResponse(HttpStatus.OK, "successfully deleted address");
    }
}
