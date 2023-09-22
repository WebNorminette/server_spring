package com.webnorm.prototypever1.service;

import com.webnorm.prototypever1.entity.member.Member;
import com.webnorm.prototypever1.entity.member.MemberAdapter;
import com.webnorm.prototypever1.security.oauth.CustomOAuth2User;
import com.webnorm.prototypever1.security.oauth.OAuthAttributes;
import com.webnorm.prototypever1.repository.MemberRepository;
import com.webnorm.prototypever1.security.oauth.SocialType;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final MemberRepository memberRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        // userRequest 에서 OAuth2User 를 추출
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);
        // registrationId : 현재 로그인 중인 서비스 구분(구글, 카카오 등)
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        // userNameAttributeName : OAuth 로그인시 키가 되는 필드값
        String userNameAttributeName = userRequest.getClientRegistration()
                .getProviderDetails()
                .getUserInfoEndpoint()
                .getUserNameAttributeName();
        // SocialType : 어떤 소셜 로그인인지 구분(registrationId 기반으로 생성)
        SocialType socialType = getSocialType(registrationId.toUpperCase());
        // authAttributes : OAuth2User 의 attribute 보관
        OAuthAttributes attributes = OAuthAttributes.of(socialType, userNameAttributeName, oAuth2User.getAttributes());

        // Member 생성 후 db에 저장 또는 업데이트
        User user = saveOrUpdate(attributes, socialType);

        // Member 로 OAuth2User 생성해서 리턴
        return new CustomOAuth2User(
                user.getAuthorities(),
                attributes.getAttributes(),
                attributes.getNameAttributeKey(),
                user.getUsername()
        );
    }

    // registrationId 로 socialType 가져오는 메서드
    private SocialType getSocialType(String registrationId) {
        if("NAVER".equals(registrationId)) {
            return SocialType.NAVER;
        }
        if("KAKAO".equals(registrationId)) {
            return SocialType.KAKAO;
        }
        return SocialType.GOOGLE;
    }

    // email 과 socialType 으로 Member 조회해서 존재하는 경우 그냥 리턴, 없는 경우 생성해서 리턴
    private User saveOrUpdate(OAuthAttributes attributes, SocialType socialType) {
        Optional<Member> findMember = memberRepository
                .findByEmailAndSocialType(attributes.getEmail(), socialType);
        if (findMember.isPresent()) return new MemberAdapter(findMember.get());
        else return new MemberAdapter(memberRepository.save(attributes.toEntity(socialType)));
    }
}
