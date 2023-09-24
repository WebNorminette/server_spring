package com.webnorm.prototypever1.dto.request.member;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.mongodb.lang.NonNull;
import com.webnorm.prototypever1.dto.request.RequestInterface;
import com.webnorm.prototypever1.entity.member.Member;
import com.webnorm.prototypever1.security.oauth.SocialType;
import lombok.*;

@AllArgsConstructor
@Getter
@Builder
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class MemberSignupRequest implements RequestInterface {
    @NonNull
    private String name;
    @NonNull
    private String email;
    @NonNull
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
