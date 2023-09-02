package com.webnorm.prototypever1.api.response;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.webnorm.prototypever1.entity.Address;
import com.webnorm.prototypever1.entity.Birth;
import com.webnorm.prototypever1.entity.Member;
import lombok.*;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class MemberListResponse {
    private String email;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private Birth birth;
    private String gender;
    private Address address;
    private int point;

    @Builder
    public MemberListResponse(String email, String firstName, String lastName,
                              String phoneNumber, Birth birth,
                              String gender, Address address, int point) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.birth = birth;
        this.gender = gender;
        this.address = address;
        this.point = point;
    }
}
