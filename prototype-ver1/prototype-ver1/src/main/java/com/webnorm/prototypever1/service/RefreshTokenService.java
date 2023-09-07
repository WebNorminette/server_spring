package com.webnorm.prototypever1.service;

import com.webnorm.prototypever1.entity.redis.RefreshToken;
import com.webnorm.prototypever1.exception.Exceptions.AuthException;
import com.webnorm.prototypever1.exception.Exceptions.BusinessLogicException;
import com.webnorm.prototypever1.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
/*
* Redis 에서 Refresh 토큰을 관리하는 Service
* */
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional
    public void saveRefreshToken(RefreshToken refreshToken) {
        refreshTokenRepository.save(refreshToken);
    }

    @Transactional
    public RefreshToken findByAccessToken(String accessToken) {
        return refreshTokenRepository.findByAccessToken(accessToken)
                .orElseThrow(() -> new BusinessLogicException(AuthException.TOKEN_NOT_FOUND));
    }

    @Transactional
    public void removeRefreshTokenByAccessToken(String accessToken) {
        RefreshToken refreshToken = refreshTokenRepository
                .findByAccessToken(accessToken)
                .orElseThrow(() -> new BusinessLogicException(AuthException.TOKEN_NOT_FOUND));
        refreshTokenRepository.delete(refreshToken);
    }
}
