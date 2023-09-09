package com.webnorm.prototypever1.security.provider;

import com.webnorm.prototypever1.exception.Exceptions.AuthException;
import com.webnorm.prototypever1.exception.Exceptions.BusinessLogicException;
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
    private final long HOUR = 60 * 60 * 1000;

    // 생성자로 secretKey 디코딩 -> 바이트 배열(keyBytes)
    public JwtTokenProvider(@Value("${jwt.secret}") String secretKey) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    // 유저 정보를 사용해 AccessToken 생성
    public String generateAccessToken(Authentication authentication) {
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));  // 권한 가져와서 저장
        long now = (new Date()).getTime();
        String accessToken = Jwts.builder()
                .setSubject(authentication.getName())
                .claim("auth", authorities)
                .setExpiration(new Date(now + HOUR))  // 만료기한 1시간으로 설정
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();     // accessToken 생성
        return accessToken;
    }

    // RefreshToken 생성
    public String generateRefreshToken() {
        long now = (new Date()).getTime();
        String refreshToken = Jwts.builder()
                .setExpiration(new Date(now + 12 * HOUR))  // 만료기한 12시간으로 설정
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();     // refreshToken 생성
        return refreshToken;
    }

    // JWT 토큰을 복호화해서 토큰 내부의 정보를 추출하는 메소드
    public Authentication getAuthentication(String accessToken) {
        Claims claims = parseClaims(accessToken);   // 토큰 복호화
        if (claims.get("auth") == null) {           // 권한 정보 없는 토큰 예외처리
            throw new BusinessLogicException(AuthException.NO_AUTH_IN_TOKEN);
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
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("exception : " + e.getClass() + " Invalid JWT Token");
            throw new JwtException("Invalid JWT Token", e);
        } catch (ExpiredJwtException e) {
            log.info("exception : " + e.getClass() + " Expired JWT Token");
            throw new JwtException("Expired JWT Token", e);
        } catch (UnsupportedJwtException e) {
            log.info("exception : " + e.getClass() + " Unsupported JWT Token");
            throw new JwtException("Unsupported JWT Token", e);
        } catch (IllegalArgumentException e) {
            log.info("exception : " + e.getClass() + " JWT claims string is empty");
            throw new JwtException("JWT claims string is empty", e);
        }
    }
}
