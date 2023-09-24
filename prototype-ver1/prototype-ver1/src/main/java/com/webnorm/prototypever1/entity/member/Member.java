package com.webnorm.prototypever1.entity.member;

import com.mongodb.lang.NonNull;
import com.webnorm.prototypever1.dto.response.member.MemberListResponse;
import com.webnorm.prototypever1.security.oauth.SocialType;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.Assert;

import java.util.*;
import java.util.stream.Collectors;

@Document(collection = "members")
@Getter
public class Member {
   @Id
   private String id;
   @NonNull
   private String email;
   @NonNull
   private String password;
   @NonNull
   private String name;
   @NonNull
   private SocialType socialType;    // 소셜로그인 구분

   @NonNull
   private List<String> roles = new ArrayList<>();


/*   // 이하는 현재 미사용중인 필드
   private String gender;
   private Msc marketingMessageConsent;
   private Address address;
   private String phoneNumber;
   private int  point;*/

    @Builder
    public Member(@NonNull String email, @NonNull String password,
                  @NonNull String name, @NonNull SocialType socialType) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.socialType = socialType;
        this.roles.add("USER");
    }

    public MemberListResponse toMemberListResponse() {
        return MemberListResponse.builder()
                .id(id)
                .email(email)
                .name(name)
                .socialType(socialType)
                .build();
    }

    // 비밀번호 encoding 메서드
    public void encodePassword(PasswordEncoder passwordEncoder) {
        this.password = passwordEncoder.encode(password);
    }

    // Member update 메서드
    public Member update(String name, String email) {
       if (name != null)    this.name = name;
       if (email != null)   this.email = email;
       return this;
    }

    public boolean compWithOriginEmail(String newEmail) {
       if (newEmail.equals(this.email)) return true;
       else return false;
    }
}

