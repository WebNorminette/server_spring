package com.webnorm.prototypever1.entity.member;

import com.mongodb.lang.NonNull;
import com.webnorm.prototypever1.exception.Exceptions.BusinessLogicException;
import com.webnorm.prototypever1.exception.Exceptions.MemberException;
import com.webnorm.prototypever1.security.oauth.SocialType;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Document(collection = "members")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member implements UserDetails {
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

   // 이하는 현재 미사용중인 필드
//   private String phoneNumber;
//   private String gender;
//   private Msc marketingMessageConsent;
//   private Address address;
//   private int  point;

   @Builder
   public Member(String email, String password, String name, SocialType socialType) {
       Assert.hasText(email, "email cannot be empty");
       Assert.hasText(name, "name cannot be empty");
       Assert.notNull(socialType, "socialType cannot be empty");
       //Assert.hasText(password, "password cannot be empty");


       this.email = email;
       this.name = name;
       this.password = password;
       this.socialType = socialType;
       this.roles.add("USER");
   }

    // 비밀번호 encoding 메서드
    public void encodePassword(PasswordEncoder passwordEncoder) {
        this.password = passwordEncoder.encode(password);
    }

//   Spring Security 인증용 UserDetails 관련 메서드 implement
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles.stream()
                .map(role -> new SimpleGrantedAuthority(role))
                .collect(Collectors.toList());
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}

