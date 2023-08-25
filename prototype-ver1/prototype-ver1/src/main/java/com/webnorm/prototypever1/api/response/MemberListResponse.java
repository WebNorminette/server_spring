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
    private String userId;
    private String userName;
    private String phoneNumber;
    private Birth birth;
    private String gender;
    private Address address;
    private int point;

    @Builder
    public MemberListResponse(String userId, String userName,
                              String phoneNumber, Birth birth,
                              String gender, Address address, int point) {
        this.userId = userId;
        this.userName = userName;
        this.phoneNumber = phoneNumber;
        this.birth = birth;
        this.gender = gender;
        this.address = address;
        this.point = point;
    }
}
