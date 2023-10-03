
package com.webnorm.prototypever1.service;

import com.webnorm.prototypever1.dto.request.address.AddressUpdateRequest;
import com.webnorm.prototypever1.entity.member.Address;
import com.webnorm.prototypever1.entity.member.Member;
import com.webnorm.prototypever1.exception.exceptions.AddressException;
import com.webnorm.prototypever1.exception.exceptions.BusinessLogicException;
import com.webnorm.prototypever1.exception.exceptions.MemberException;
import com.webnorm.prototypever1.repository.AddressRepository;
import com.webnorm.prototypever1.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class AddressService {
    private final AddressRepository addressRepository;
    private final MemberRepository memberRepository;

    /*
     * [주소 추가]
     * */
    public Address createAddress(Address address) {
        return addressRepository.save(address);
    }

    // id 로 조회
    public Address findById(String addressId) {
        return addressRepository.findById(addressId)
                .orElseThrow(() -> new BusinessLogicException(AddressException.ADDRESS_NOT_FOUND));
    }

    /*
     * [주소 수정]
     * */
    public Address updateAddress(String addressId, AddressUpdateRequest request) {
        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new BusinessLogicException(AddressException.ADDRESS_NOT_FOUND));
        Address updatedAddress = address.update(request.toEntity());
        return addressRepository.save(updatedAddress);
    }
}
