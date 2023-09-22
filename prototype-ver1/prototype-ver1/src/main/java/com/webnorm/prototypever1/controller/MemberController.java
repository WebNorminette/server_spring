package com.webnorm.prototypever1.controller;

import com.webnorm.prototypever1.api.request.MemberLoginRequest;
import com.webnorm.prototypever1.api.request.MemberSignupRequest;
import com.webnorm.prototypever1.api.request.MemberUpdateRequest;
import com.webnorm.prototypever1.api.response.MemberListResponse;
import com.webnorm.prototypever1.api.response.MultiResponse;
import com.webnorm.prototypever1.api.response.SingleResponse;
import com.webnorm.prototypever1.entity.member.Member;
import com.webnorm.prototypever1.entity.member.MemberAdapter;
import com.webnorm.prototypever1.exception.exceptions.MemberException;
import com.webnorm.prototypever1.security.redis.RedisTokenInfo;
import com.webnorm.prototypever1.exception.exceptions.AuthException;
import com.webnorm.prototypever1.exception.exceptions.BusinessLogicException;
import com.webnorm.prototypever1.security.oauth.SocialType;
import com.webnorm.prototypever1.service.MemberService;
import com.webnorm.prototypever1.security.TokenInfo;
import com.webnorm.prototypever1.service.RedisTokenInfoService;
import com.webnorm.prototypever1.util.DataPattern;
import com.webnorm.prototypever1.util.DataPatternMatcher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
@Slf4j
public class MemberController {
    private final MemberService memberService;
    private final EmailController emailController;
    private final RedisTokenInfoService redisTokenInfoService;

    // 회원가입 : request(dto)를 member 엔티티에 매핑, 메일 발송
    @PostMapping
    public SingleResponse signup(@RequestBody MemberSignupRequest request) {
        DataPatternMatcher.doesMatch(request.getEmail(), DataPattern.EMAIL);
        DataPatternMatcher.doesMatch(request.getName(), DataPattern.NAME);
        DataPatternMatcher.doesMatch(request.getPassword(), DataPattern.PASSWORD);
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
                        .id(m.getId())
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

    // jwt token 재발급 (reissue) : atk 만료시 rtk 로 재발급 -> atk, rtk 모두 재발급 실행
    @GetMapping("/reissue")
    public SingleResponse reissue(@RequestHeader("Authorization") String refreshToken) {
        // rtk 추출
        String resolvedRefreshToken;
        if (StringUtils.hasText(refreshToken) && refreshToken.startsWith("Bearer"))
            resolvedRefreshToken = refreshToken.substring(7);    // "Bearer " 부분을 제거하여 실제 토큰값만 추출
        else throw new BusinessLogicException(AuthException.MALFORMED_TOKEN);
        // rtk로 tokenInfo 조회
        RedisTokenInfo redisTokenInfo = redisTokenInfoService.findByRefreshToken(resolvedRefreshToken);
        // refeshService 의 메서드에 atk, rtk 주면 검증해서 새로운 atk 를 발급받고, 이를 tokenInfo 로 감싸서 응답
        TokenInfo tokenInfo = memberService.reissueToken(redisTokenInfo.getRefreshToken(),
                redisTokenInfo.getAccessToken());
        // redis 에 RefreshToken 업데이트
        redisTokenInfo.update(tokenInfo.getAccessToken(), tokenInfo.getRefreshToken());
        redisTokenInfoService.saveTokenInfo(redisTokenInfo);
        // tokenInfo Response
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
        redisTokenInfoService.removeRefreshTokenByAccessToken(resolvedAccessToken);
        return new SingleResponse(HttpStatus.OK, "logout success");
    }

    // 회원정보 수정 (email, name만 수정 가능)
    @PutMapping("/update")
    public SingleResponse update(@AuthenticationPrincipal MemberAdapter memberAdapter,
                                 @RequestBody MemberUpdateRequest request) {
        // 현재 로그인한 사용자 불러오기
        Member member = memberAdapter.getMember();
        // update
        Member updatedMember = memberService.updateMember(member, request);
        return new SingleResponse(HttpStatus.OK, "successfully updated member " + updatedMember.getId(), updatedMember);
    }

    // 회원 탈퇴
    @DeleteMapping("/{memberId}")
    public SingleResponse deleteForAdmin(@PathVariable("memberId") String memberId) {
        // id 로 사용자 삭제
        Member deletedMember = memberService.deleteMember(memberId);
        return new SingleResponse(HttpStatus.OK, "successfully deleted member " + deletedMember.getId());
    }
}
