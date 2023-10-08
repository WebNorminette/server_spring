package com.webnorm.prototypever1.dto.response.address;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.webnorm.prototypever1.entity.member.Address;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class AddressListResponse {
    private Address defaultAddress;
    private List<Address> addressList;
}
