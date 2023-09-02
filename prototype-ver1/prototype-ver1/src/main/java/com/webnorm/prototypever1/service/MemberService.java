package com.webnorm.prototypever1.service;

import com.webnorm.prototypever1.entity.Member;
import com.webnorm.prototypever1.exception.exceptions.MemberEmailDuplicateException;
import com.webnorm.prototypever1.repository.MemberRepository;
import com.webnorm.prototypever1.security.provider.JwtTokenProvider;
import com.webnorm.prototypever1.security.TokenInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
@Slf4j
public class MemberService {
    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;

    /*
    * [회원가입]
    * # 아이디(이메일) 중복검사
    * # 비밀번호 인코딩
    * # 이메일 전송
    */
    public Member createMember(Member member) {
        member.encodePassword(passwordEncoder);
        Optional<Member> findMember = memberRepository.findByEmail(member.getEmail());
        if(findMember.isPresent())
            throw new RuntimeException("member email duplicated");
        return memberRepository.save(member);
    }

    // 회원목록 조회(관리자)
    public List<Member> findAllMember() {
        return memberRepository.findAll();
    }

    /*
     * [로그인]
     * */
    public TokenInfo login(String memberId, String password) {
        // login id, pw 값을 넣어 Authentication 객체 생성 (authenticated = false)
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(memberId, password);
        // 사용자 인증 (id로 사용자를 불러와 pw 체크)
        Authentication authentication = null;
        authentication = authenticationManagerBuilder
                .getObject()
                .authenticate(authenticationToken);
        // 인증 결과를 넣어 jwt 토큰 생성
        TokenInfo tokenInfo = jwtTokenProvider.generateToken(authentication);
        return tokenInfo;
    }
}
