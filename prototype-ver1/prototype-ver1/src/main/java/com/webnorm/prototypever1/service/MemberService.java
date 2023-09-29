package com.webnorm.prototypever1.service;

import com.webnorm.prototypever1.dto.request.member.MemberUpdateRequest;
import com.webnorm.prototypever1.entity.member.Member;
import com.webnorm.prototypever1.security.oauth.SocialType;
import com.webnorm.prototypever1.security.redis.RedisTokenInfo;
import com.webnorm.prototypever1.exception.exceptions.AuthException;
import com.webnorm.prototypever1.exception.exceptions.BusinessLogicException;
import com.webnorm.prototypever1.exception.exceptions.MemberException;
import com.webnorm.prototypever1.repository.MemberRepository;
import com.webnorm.prototypever1.repository.RedisTokenInfoRepository;
import com.webnorm.prototypever1.security.provider.JwtTokenProvider;
import com.webnorm.prototypever1.security.TokenInfo;
import com.webnorm.prototypever1.util.DataPattern;
import com.webnorm.prototypever1.util.DataPatternMatcher;
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
    private final RedisTokenInfoRepository redisTokenInfoRepository;

    /*
    * [회원가입]
    * # 아이디(이메일) 중복검사
    * # 비밀번호 인코딩
    * # 이메일 전송
    */
    public Member saveMember(Member member) {
        member.encodePassword(passwordEncoder);
        Optional<Member> findMember = memberRepository.findByEmailAndSocialType(member.getEmail(), member.getSocialType());
        if(findMember.isPresent())
            throw new BusinessLogicException(MemberException.EMAIL_DUP);
        return memberRepository.save(member);
    }

    // 회원목록 조회(관리자)
    public List<Member> findAllMember() {
        return memberRepository.findAll();
    }

    /*
     * [로그인] : 일반 로그인(ORIGIN)
     * */
    public TokenInfo login(String memberId, String password) {
        // login id, pw 값을 넣어 Authentication 객체 생성 (authenticated = false)
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(memberId, password);
        // 사용자 인증 (id로 사용자를 불러와 pw 체크)
        Authentication authentication = authenticationManagerBuilder
                .getObject()
                .authenticate(authenticationToken);
        // 인증 결과를 넣어 atk 생성
        String accessToken = jwtTokenProvider
                .generateAccessToken(authentication, SocialType.ORIGIN, memberId);
        // rtk 생성
        String refreshToken = jwtTokenProvider.generateRefreshToken();
        // redis 에 rtk, atk, memberId 세트로 저장
        RedisTokenInfo savedRedisTokenInfo = redisTokenInfoRepository.save(
                RedisTokenInfo.builder()
                        .id(memberId)
                        .refreshToken(refreshToken)
                        .accessToken(accessToken)
                        .build()
        );
        // TokenInfo 생성 후 리턴
        TokenInfo tokenInfo = TokenInfo.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .grantType("Bearer")
                .build();
        return tokenInfo;
    }

    /*
     * [ATK 재발급]
     * RTK 를 받아 유효성 검증
     * 유효한 경우 새로운 ATK, RTK 리턴
     * */
    public TokenInfo reissueToken(String refreshToken, String accessToken) {
        // rtk 가 존재하고 rtk, atk 모두 유효한 경우
        if (refreshToken != null && jwtTokenProvider.validateToken(refreshToken)
                && jwtTokenProvider.validateToken(accessToken)) {
            // Authentication 객체 생성 (ATK 기반)
            Authentication authentication = jwtTokenProvider.getAuthentication(accessToken);
            // Authentication 객체 기반으로 새 ATK 생성
            String newAccessToken = jwtTokenProvider
                    .regenerateAccessTokenByAccessToken(authentication, accessToken);
            // 새 RTK 생성
            String newRefreshToken = jwtTokenProvider.generateRefreshToken();
            TokenInfo tokenInfo = TokenInfo.builder()
                    .grantType("Bearer")
                    .accessToken(newAccessToken)
                    .refreshToken(newRefreshToken)
                    .build();
            return tokenInfo;
        } else
            throw new BusinessLogicException(AuthException.TOKEN_NOT_FOUND);
    }

    /*
     * [회원정보 수정]
     */
    public Member updateMember(String memberId, MemberUpdateRequest request) {
        // 사용자 id로 불러오기
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessLogicException(MemberException.USER_NOT_FOUND));

        // 형식 체크
        DataPatternMatcher.doesMatch(request.getName(), DataPattern.NAME);
        DataPatternMatcher.doesMatch(request.getEmail(), DataPattern.EMAIL);

        // email 중복체크 (email 변경 요청시에만 -> 기존 email 과 상이한 경우)
        if (!member.compWithOriginEmail(request.getEmail())) {
            //log.info("이메일 상이! -> 중복검ㅅㅏ");
            if (memberRepository.findByEmail(member.getEmail()).isPresent())
                throw new BusinessLogicException(MemberException.EMAIL_DUP);
        }

        Member updatedMember = member.update(       // 수정할 Member 객체 생성
                request.getName(),
                request.getEmail()
        );
        return memberRepository.save(updatedMember);
    }

    /*
     * [회원 삭제/탈퇴]
     */
    public Member deleteMember(String memberId) {
        // id 로 회원 조회
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessLogicException(MemberException.USER_NOT_FOUND));

        memberRepository.delete(member);
        return member;
    }
}
