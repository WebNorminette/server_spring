package com.webnorm.prototypever1.security.filter;

import com.webnorm.prototypever1.security.provider.JwtTokenProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    // filtering 메서드 override
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//        if ("/members/login".equals(request.getRequestURI()) && "POST".equals(request.getMethod())) {
//            filterChain.doFilter(request, response);
//            return;
//        }
        String token = resolveToken(request);  // Request Header 에서 JWT 토큰값만 추출

        if (token != null && jwtTokenProvider.validateToken(token)) {   // 토큰 유효성 검사
            // 토큰이 유효한 경우 토큰에서 Authentication 객체를 가져와 SecurityContext에 저장
            Authentication authentication = jwtTokenProvider.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(request, response);  // 다음 필터 실행
    }

    // Request Header 에서 토큰 정보 추출하는 메서드
    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer"))
            return bearerToken.substring(7);    // "Bearer " 부분을 제거하여 실제 토큰값만 추출
        else
            return null;
    }
}
