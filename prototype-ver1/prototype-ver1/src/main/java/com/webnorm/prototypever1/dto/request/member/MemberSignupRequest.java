package com.webnorm.prototypever1.dto.request.member;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.mongodb.lang.NonNull;
import com.webnorm.prototypever1.dto.request.RequestInterface;
import com.webnorm.prototypever1.entity.member.Member;
import com.webnorm.prototypever1.security.oauth.SocialType;
import lombok.*;

@Data
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class MemberSignupRequest implements RequestInterface {
    private String name;
    private String email;
    private String password;

    public Member toEntity() {
        return Member.builder()
                .name(name)
                .email(email)
                .password(password)
                .socialType(SocialType.ORIGIN)
                .build();
    }
}
