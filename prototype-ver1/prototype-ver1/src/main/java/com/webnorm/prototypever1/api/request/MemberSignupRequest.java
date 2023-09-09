package com.webnorm.prototypever1.api.request;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.mongodb.lang.NonNull;
import com.webnorm.prototypever1.security.oauth.SocialType;
import lombok.*;
import org.springframework.util.Assert;

@Data
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class MemberSignupRequest {
    @NonNull
    private String name;
    @NonNull
    private String email;
    @NonNull
    private String password;
    @NonNull
    private SocialType socialType;

    @Builder
    public MemberSignupRequest(@NonNull String name,
                               @NonNull String email,
                               @NonNull String password) {

        Assert.hasText(email, "email cannot be empty");
        Assert.hasText(name, "name cannot be empty");
        Assert.hasText(password, "password cannot be empty");

        this.name = name;
        this.email = email;
        this.password = password;
        this.socialType = SocialType.ORIGIN;
    }
}
