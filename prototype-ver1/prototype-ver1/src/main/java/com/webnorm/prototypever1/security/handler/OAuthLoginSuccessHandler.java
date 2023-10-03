package com.webnorm.prototypever1.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.webnorm.prototypever1.security.redis.RedisTokenInfo;
import com.webnorm.prototypever1.repository.RedisTokenInfoRepository;
import com.webnorm.prototypever1.security.TokenInfo;
import com.webnorm.prototypever1.security.oauth.CustomOAuth2User;
import com.webnorm.prototypever1.security.provider.JwtTokenProvider;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuthLoginSuccessHandler implements AuthenticationSuccessHandler {
    private final JwtTokenProvider jwtTokenProvider;
    private final RedisTokenInfoRepository redisTokenInfoRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        log.info("OAuth 로그인 성공!");
        CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();

        // TokenInfo 생성
        TokenInfo tokenInfo = getTokenInfo(authentication, oAuth2User);

        // response 세팅
        response.setContentType("application/json; charset=UTF-8");
        response.setStatus(HttpStatus.OK.value());
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("timeStamp", LocalDateTime.now().toString());
        responseBody.put("status", HttpStatus.OK);
        responseBody.put("message", "oauth login success");
        responseBody.put("data", tokenInfo);
        response.getWriter().write(new ObjectMapper().writeValueAsString(responseBody));
    }

    private TokenInfo getTokenInfo(Authentication authentication, CustomOAuth2User oAuth2User) {
        // 인증 결과를 넣어 atk 생성
        String accessToken = jwtTokenProvider
                .generateAccessToken(authentication, oAuth2User.getSocialType(), oAuth2User.getEmail());
        // refresh token 생성
        String refreshToken = jwtTokenProvider.generateRefreshToken();
        // redis 에 Token 저장
        redisTokenInfoRepository.save(
                RedisTokenInfo.builder()
                        .id(oAuth2User.getEmail())
                        .refreshToken(refreshToken)
                        .accessToken(accessToken)
                        .build()
        );
        // TokenInfo 생성 후 리턴
        return TokenInfo.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .grantType("Bearer")
                .build();
    }
}
