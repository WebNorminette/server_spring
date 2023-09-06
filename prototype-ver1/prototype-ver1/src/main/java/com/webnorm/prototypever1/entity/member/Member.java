package com.webnorm.prototypever1.entity.member;

import com.mongodb.lang.NonNull;
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
   private String lastName;
   @NonNull
   private String firstName;

   @NonNull
   private List<String> roles = new ArrayList<>();

   private String phoneNumber;
   private Birth birth;
   private String gender;
   private Msc marketingMessageConsent;

   private Address address;
   private int  point;

   @Builder
   public Member(String email, String password, String lastName, String firstName,
                 String phoneNumber, Birth birth, String gender, Msc marketingMessageConsent) {
       Assert.hasText(email, "email cannot be empty");
       Assert.hasText(lastName, "firstName cannot be empty");
       Assert.hasText(firstName, "lastName cannot be empty");
       Assert.hasText(password, "password cannot be empty");

       this.email = email;
       this.lastName = lastName;
       this.firstName = firstName;
       this.password = password;
       this.phoneNumber = phoneNumber;
       this.birth = birth;
       this.gender = gender;
       this.marketingMessageConsent = marketingMessageConsent;
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

