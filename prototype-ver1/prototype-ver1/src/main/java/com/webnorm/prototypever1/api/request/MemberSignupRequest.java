package com.webnorm.prototypever1.api.request;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.webnorm.prototypever1.entity.Birth;
import com.webnorm.prototypever1.entity.Msc;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class MemberSignupRequest {
    private String userName;
    private String userId;
    private String password;
    private String gender;
    private Birth birth;
    private String phoneNumber;
    private Msc marketingMessageConsent;
}
