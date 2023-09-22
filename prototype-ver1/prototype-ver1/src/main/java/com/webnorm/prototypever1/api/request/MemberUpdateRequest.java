package com.webnorm.prototypever1.api.request;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.mongodb.lang.NonNull;
import com.webnorm.prototypever1.security.oauth.SocialType;
import lombok.Builder;
import lombok.Data;

import java.util.Optional;

@Data
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class MemberUpdateRequest {
    private String name;
    private String email;

    @Builder
    public MemberUpdateRequest(@NonNull String name, @NonNull String email) {
        this.name = name;
        this.email = email;
    }
}
