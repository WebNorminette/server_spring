package com.webnorm.prototypever1.entity;

import com.mongodb.lang.NonNull;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.util.Assert;

import java.util.Optional;

@Document(collection = "members")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {
   @Id
   private String id;
   @NonNull
   private String userId;
   @NonNull
   private String password;
   @NonNull
   private String userName;
   @NonNull
   private String phoneNumber;
   @NonNull
   private Birth birth;
   @NonNull
   private String gender;
   @NonNull
   private Msc marketingMessageConsent;

   private Address address;
   private int  point;

   @Builder
   public Member(String userId, String password, String userName,
                 String phoneNumber, Birth birth, String gender, Msc marketingMessageConsent) {
       Assert.hasText(userId, "userId cannot be empty");
       Assert.hasText(userName, "userName cannot be empty");
       Assert.hasText(password, "password cannot be empty");
       Assert.hasText(phoneNumber, "phoneNumber cannot be empty");
       Assert.notNull(birth, "birth cannot be empty");
       Assert.hasText(gender, "gender cannot be empty");
       Assert.notNull(marketingMessageConsent, "marketingMessageConsent cannot be empty");

       this.userId = userId;
       this.userName = userName;
       this.password = password;
       this.phoneNumber = phoneNumber;
       this.birth = birth;
       this.gender = gender;
       this.marketingMessageConsent = marketingMessageConsent;
   }
}

