package com.webnorm.prototypever1.controller;

import com.webnorm.prototypever1.api.request.MemberLoginRequest;
import com.webnorm.prototypever1.api.request.MemberSignupRequest;
import com.webnorm.prototypever1.api.response.MemberListResponse;
import com.webnorm.prototypever1.api.response.MultiResponse;
import com.webnorm.prototypever1.api.response.SingleResponse;
import com.webnorm.prototypever1.entity.member.Member;
import com.webnorm.prototypever1.entity.redis.RefreshToken;
import com.webnorm.prototypever1.exception.Exceptions.AuthException;
import com.webnorm.prototypever1.exception.Exceptions.BusinessLogicException;
import com.webnorm.prototypever1.security.oauth.SocialType;
import com.webnorm.prototypever1.service.MemberService;
import com.webnorm.prototypever1.security.TokenInfo;
import com.webnorm.prototypever1.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
@Slf4j
public class MemberController {
    private final MemberService memberService;
    private final EmailController emailController;
    private final RefreshTokenService refreshTokenService;

    // 회원가입 : request(dto)를 member 엔티티에 매핑, 메일 발송
    @PostMapping
    public SingleResponse signup(@RequestBody MemberSignupRequest request) {
        Member member = Member.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(request.getPassword())
                .socialType(SocialType.ORIGIN)
                .build();
        memberService.createMember(member);
        emailController.sendWelcomeEmail(member.getEmail(), member.getName());
        return new SingleResponse(HttpStatus.OK, "signup success");
    }

    // 회원목록 조회(관리자) : response(dto) 리스트를 stream을 이용해 member 리스트로 매핑
    @GetMapping
    //@PreAuthorize("hasAuthority('USER')")
    public MultiResponse memberList() {
        List<Member> findMembers = memberService.findAllMember();
        List<MemberListResponse> memberList = findMembers.stream()
                .map(m -> MemberListResponse.builder()
                        .email(m.getEmail())
                        .name(m.getName())
                        .socialType(m.getSocialType())
                        .build())
                .collect(Collectors.toList());
        return new MultiResponse(HttpStatus.OK, "successfully found memberList", memberList);
    }

    // 로그인
    @PostMapping("/login")
    public SingleResponse login(@RequestBody MemberLoginRequest request) {
        String email = request.getEmail();
        String password = request.getPassword();
        TokenInfo tokenInfo = memberService.login(email, password);
        return new SingleResponse(HttpStatus.OK, "login success", tokenInfo);
    }

    // access token 재발급 (reissue) : atk 만료시 rtk 로 재발급
    @GetMapping("/reissue")
    public SingleResponse reissue(@RequestHeader("Authorization") String accessToken) {
        // atk 추출
        String resolvedAccessToken;
        if (StringUtils.hasText(accessToken) && accessToken.startsWith("Bearer"))
            resolvedAccessToken = accessToken.substring(7);    // "Bearer " 부분을 제거하여 실제 토큰값만 추출
        else throw new BusinessLogicException(AuthException.MALFORMED_TOKEN);
        // atk로 rtk 조회
        RefreshToken refreshToken = refreshTokenService.findByAccessToken(resolvedAccessToken);
        // refeshService 의 메서드에 atk, rtk 주면 검증해서 새로운 atk 를 발급받고, 이를 tokenInfo 로 감싸서 응답
        TokenInfo tokenInfo = memberService.reissueToken(refreshToken.getRefreshToken(), resolvedAccessToken);
        // redis 에 RefreshToken 업데이트 후 반영
        refreshToken.updateAccessToken(tokenInfo.getAccessToken());
        refreshTokenService.saveRefreshToken(refreshToken);
        return new SingleResponse(HttpStatus.OK, "reissue success", tokenInfo);
    }

    // 로그아웃
    @GetMapping("/logout")
    public SingleResponse logout(@RequestHeader("Authorization") String accessToken) {
        // atk 추출
        String resolvedAccessToken;
        if (StringUtils.hasText(accessToken) && accessToken.startsWith("Bearer"))
            resolvedAccessToken = accessToken.substring(7);    // "Bearer " 부분을 제거하여 실제 토큰값만 추출
        else throw new BusinessLogicException(AuthException.MALFORMED_TOKEN);
        // atk 로 rtk 조회 후 redis 에서 삭제
        refreshTokenService.removeRefreshTokenByAccessToken(resolvedAccessToken);
        return new SingleResponse(HttpStatus.OK, "logout success");
    }
}
