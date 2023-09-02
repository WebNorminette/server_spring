package com.webnorm.prototypever1.api.request;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.mongodb.lang.NonNull;
import com.webnorm.prototypever1.entity.Birth;
import com.webnorm.prototypever1.entity.Msc;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.util.Assert;

@Data
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class MemberSignupRequest {
    @NonNull
    private String lastName;
    @NonNull
    private String firstName;
    @NonNull
    private String email;
    @NonNull
    private String password;

    @Builder
    public MemberSignupRequest(@NonNull String lastName,
                               @NonNull String firstName,
                               @NonNull String email,
                               @NonNull String password) {

        this.lastName = lastName;
        this.firstName = firstName;
        this.email = email;
        this.password = password;
    }
}
