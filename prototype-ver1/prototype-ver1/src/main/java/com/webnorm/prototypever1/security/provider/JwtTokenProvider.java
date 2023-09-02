package com.webnorm.prototypever1.security.provider;

import com.webnorm.prototypever1.security.TokenInfo;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Slf4j
@Component
public class JwtTokenProvider {
    private final Key key;

    // 생성자로 secretKey 디코딩 -> 바이트 배열(keyBytes)
    public JwtTokenProvider(@Value("${jwt.secret}") String secretKey) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    // 유저 정보를 사용해 AccessToken과 RefreshToken 생성하는 메소드
    public TokenInfo generateToken(Authentication authentication) {
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));  // 권한 가져와서 저장
        long now = (new Date()).getTime();
        String accessToken = Jwts.builder()
                .setSubject(authentication.getName())
                .claim("auth", authorities)
                .setExpiration(new Date(now + 60 * 60 * 1000))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();     // accessToken 생성
        String refreshToken = Jwts.builder()
                .setExpiration(new Date(now + 60 * 60 * 1000))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();     // refreshToken 생성
        return TokenInfo.builder()
                .grantType("Bearer")
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    // JWT 토큰을 복호화해서 토큰 내부의 정보를 추출하는 메소드
    public Authentication getAuthentication(String accessToken) {
        Claims claims = parseClaims(accessToken);   // 토큰 복호화
        if (claims.get("auth") == null) {           // 권한 정보 없는 토큰 예외처리
            throw new RuntimeException("권한 정보가 없는 토큰입니다.");
        }
        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get("auth").toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());      // 클레임에서 권한 정보 리스트로 받아오기
        UserDetails principal = new User(claims.getSubject(), "", authorities);
        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }

    // jwt 파싱해서 claim 생성하는 메소드
    private Claims parseClaims(String accessToken) {
        try {
            return Jwts.parserBuilder().
                    setSigningKey(key).
                    build().
                    parseClaimsJws(accessToken).
                    getBody();
        } catch (ExpiredJwtException e) {   // token expire : 유효기간 만료 예외처리
            return e.getClaims();
        }
    }

    // 토큰 정보를 검증하는 메소드
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);     // 토큰 유효성 확인 및 서명 검증
            return true;
        } catch (Exception e) {
            throw e;
        }
    }
}
