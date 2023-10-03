package com.webnorm.prototypever1.controller;

import com.webnorm.prototypever1.dto.request.member.MemberLoginRequest;
import com.webnorm.prototypever1.dto.request.member.MemberSignupRequest;
import com.webnorm.prototypever1.dto.request.member.MemberUpdatePasswordRequest;
import com.webnorm.prototypever1.dto.request.member.MemberUpdateRequest;
import com.webnorm.prototypever1.dto.response.member.MemberListResponse;
import com.webnorm.prototypever1.dto.response.MultiResponse;
import com.webnorm.prototypever1.dto.response.SingleResponse;
import com.webnorm.prototypever1.entity.member.Member;
import com.webnorm.prototypever1.exception.exceptions.MemberException;
import com.webnorm.prototypever1.security.redis.RedisTokenInfo;
import com.webnorm.prototypever1.exception.exceptions.AuthException;
import com.webnorm.prototypever1.exception.exceptions.BusinessLogicException;
import com.webnorm.prototypever1.service.MemberService;
import com.webnorm.prototypever1.security.TokenInfo;
import com.webnorm.prototypever1.service.RedisTokenInfoService;
import com.webnorm.prototypever1.util.DataPattern;
import com.webnorm.prototypever1.util.DataPatternMatcher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members")
@Slf4j
public class MemberController {
    private final MemberService memberService;
    private final EmailController emailController;
    private final RedisTokenInfoService redisTokenInfoService;

    // 회원가입 : request(dto)를 member 엔티티에 매핑, 메일 발송
    @PostMapping
    public SingleResponse signup(@RequestBody MemberSignupRequest request) {
        // 데이터 형식검사
        DataPatternMatcher.doesMatch(request.getEmail(), DataPattern.EMAIL);
        DataPatternMatcher.doesMatch(request.getName(), DataPattern.NAME);
        DataPatternMatcher.doesMatch(request.getPassword(), DataPattern.PASSWORD);
        // dto -> entity 전환
        Member member = request.toEntity();
        // service 로 넘김
        memberService.saveMember(member);
        // 가입 확인용 email 전송
        emailController.sendWelcomeEmail(member.getEmail(), member.getName());
        return new SingleResponse(HttpStatus.OK, "signup success");
    }

    // 회원목록 조회(관리자) : response(dto) 리스트를 stream을 이용해 member 리스트로 매핑
    @GetMapping
    //@PreAuthorize("hasAuthority('USER')")
    public MultiResponse memberList() {
        // 전체 회원 조회
        List<Member> findMembers = memberService.findAllMember();
        // entity -> dto 리스트로 전환
        List<MemberListResponse> memberList = findMembers.stream()
                .map(m -> m.toMemberListResponse())
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
        RedisTokenInfo updatedRedisTokenInfo = redisTokenInfo.update(tokenInfo.getAccessToken(), tokenInfo.getRefreshToken());
        redisTokenInfoService.saveTokenInfo(updatedRedisTokenInfo);
        // tokenInfo Response
        return new SingleResponse(HttpStatus.OK, "reissue success", updatedRedisTokenInfo);
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
        redisTokenInfoService.removeByAccessToken(resolvedAccessToken);
        return new SingleResponse(HttpStatus.OK, "logout success");
    }

    // 회원정보 수정 (email, name 수정용)
    @PutMapping("/update/{memberId}")
    public SingleResponse update(
            @PathVariable("memberId") String memberId,
            @RequestBody MemberUpdateRequest request) {
        // id로 회원 조회 후 수정
        Member updatedMember = memberService.updateMember(memberId, request);
        return new SingleResponse(HttpStatus.OK, "successfully updated member " + updatedMember.getName());
    }

    // 회원정보 수정 (password 수정용)
    @PutMapping("/update-password/{memberId}")
    public SingleResponse updatePassword(
            @PathVariable("memberId") String memberId,
            @RequestBody MemberUpdatePasswordRequest request
    ) {
        // id 로 회원 조회 후 수정
        Member updatedMember = memberService.updatePassword(memberId, request.getPassword());
        return new SingleResponse(HttpStatus.OK, "successfully updated password from member " + updatedMember.getName());
    }

    // 회원 탈퇴
    @DeleteMapping("/{memberId}")
    public SingleResponse deleteForAdmin(@PathVariable("memberId") String memberId) {
        // id 로 사용자 삭제
        Member deletedMember = memberService.deleteMember(memberId);
        return new SingleResponse(HttpStatus.OK, "successfully deleted member " + deletedMember.getId());
    }
}
