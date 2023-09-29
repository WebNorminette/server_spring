package com.webnorm.prototypever1.service;

import com.webnorm.prototypever1.entity.member.Member;
import com.webnorm.prototypever1.entity.member.MemberAdapter;
import com.webnorm.prototypever1.exception.exceptions.BusinessLogicException;
import com.webnorm.prototypever1.exception.exceptions.MemberException;
import com.webnorm.prototypever1.repository.MemberRepository;
import com.webnorm.prototypever1.security.oauth.SocialType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomUserDetailService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    // authenticate() 실행시 이 메서드 실행 : username 으로 사용자 찾아서 인증 -> ORIGIN 로그인시 사용됨
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Member> optionalMember = memberRepository.findByEmail(username);
        Member findMember = optionalMember.orElseThrow(() -> new BusinessLogicException(MemberException.USER_NOT_FOUND));
        log.info("user : " + findMember.getName() + " from " + findMember.getSocialType() + " login success");
        return new MemberAdapter(findMember);
    }

    /*// socialType 과 같이 조회하는 메서드 -> Authentication 객체 불러올 때 사용
    public UserDetails loadUserByUsername(String username, SocialType socialType) throws UsernameNotFoundException{
        Member findMember = memberRepository.findByEmailAndSocialType(username, socialType)
                .orElseThrow(() -> new BusinessLogicException(MemberException.USER_NOT_FOUND));
        log.info("user : " + findMember.getName() + " from " + findMember.getSocialType() + "login success!");
        return new MemberAdapter(findMember);
    }*/
}
