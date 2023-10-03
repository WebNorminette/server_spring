package com.webnorm.prototypever1.dto.request.member;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.Data;

@Data
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class MemberUpdatePasswordRequest {
    private String password;
}
