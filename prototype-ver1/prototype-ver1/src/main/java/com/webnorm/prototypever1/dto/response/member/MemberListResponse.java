package com.webnorm.prototypever1.dto.response.member;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.webnorm.prototypever1.entity.member.Member;
import com.webnorm.prototypever1.security.oauth.SocialType;
import lombok.*;
import org.springframework.data.domain.Page;


@Data
@AllArgsConstructor
@Builder
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class MemberListResponse {
    private String id;
    private String email;
    private String name;
    private SocialType socialType;
}
