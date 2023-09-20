package com.webnorm.prototypever1.security.oauth;

import com.webnorm.prototypever1.entity.member.Member;
import com.webnorm.prototypever1.exception.exceptions.AuthException;
import com.webnorm.prototypever1.exception.exceptions.BusinessLogicException;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;
import java.util.UUID;

@Getter
public class OAuthAttributes {
    private Map<String, Object> attributes;
    private String nameAttributeKey;
    private String name;
    private String email;

    @Builder
    public OAuthAttributes(Map<String, Object> attributes, String nameAttributeKey,
                           String name, String email) {
        this.attributes = attributes;
        this.nameAttributeKey = nameAttributeKey;
        this.name = name;
        this.email = email;
    }

    //소셜 타입에 맞는 메서드 호출해서 OAuthAttributes 객체 반환
    public static OAuthAttributes of(SocialType socialType, String userNameAttributeName,
                                     Map<String, Object> attributes) {
        if (socialType == SocialType.NAVER)
            return ofNaver(userNameAttributeName, attributes);
        else if (socialType == SocialType.KAKAO)
            return ofKakao(userNameAttributeName, attributes);
        else
            return ofGoogle(userNameAttributeName, attributes);
    }

    // Naver 에서 제공한 유저 정보를 OAuthAttributes 로 매핑해서 확인
    public static OAuthAttributes ofNaver(String userNameAttributeName, Map<String, Object> attributes) {
        // name 찾기
        Map<String, Object> response = (Map<String, Object>) attributes.get("response");
        String name = (String) response.get("name");
        if (name.isEmpty()) throw new BusinessLogicException(AuthException.OAUTH_CANOOT_FIND_USERNAME);
        return OAuthAttributes.builder()
                .name(name)
                .email(UUID.randomUUID() + "@naverUser.com")
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }

    // KaKao 에서 제공한 유저 정보를 OAuthAttributes 로 매핑해서 리턴
    private static OAuthAttributes ofKakao(String userNameAttributeName, Map<String, Object> attributes) {
        // nickname 찾기
        Map<String, Object> response = (Map<String, Object>) attributes.get("kakao_account");
        Map<String, Object> profile = (Map<String, Object>) response.get("profile");
        String nickname = (String) profile.get("nickname");
        if (nickname.isEmpty()) throw new BusinessLogicException(AuthException.OAUTH_CANOOT_FIND_USERNAME);
        return OAuthAttributes.builder()
                .name(nickname)
                .email(UUID.randomUUID() + "@kakaoUser.com")
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }

    // Google 에서 제공한 유저 정보를 OAuthAttributes 로 매핑해서 리턴
    private static OAuthAttributes ofGoogle(String userNameAttributeName, Map<String, Object> attributes) {
        return OAuthAttributes.builder()
                .name((String) attributes.get("name"))
                .email((String) attributes.get("email"))
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }

    public Member toEntity(SocialType socialType) {
        return Member.builder()
                .email(email)
                .name(name)
                .socialType(socialType)
                .build();
    }
}
