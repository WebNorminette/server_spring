package com.webnorm.prototypever1.service;

import com.webnorm.prototypever1.security.redis.RedisTokenInfo;
import com.webnorm.prototypever1.exception.exceptions.AuthException;
import com.webnorm.prototypever1.exception.exceptions.BusinessLogicException;
import com.webnorm.prototypever1.repository.RedisTokenInfoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
/*
* Redis 에서 Refresh 토큰을 관리하는 Service
* */
public class RedisTokenInfoService {
    private final RedisTokenInfoRepository redisTokenInfoRepository;

    public void saveTokenInfo(RedisTokenInfo redisTokenInfo) {
        redisTokenInfoRepository.save(redisTokenInfo);
    }

    public RedisTokenInfo findByRefreshToken(String refreshToken) {
        return redisTokenInfoRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new BusinessLogicException(AuthException.TOKEN_NOT_FOUND));
    }

    public void removeRefreshTokenByAccessToken(String accessToken) {
        RedisTokenInfo redisTokenInfo = redisTokenInfoRepository
                .findByAccessToken(accessToken)
                .orElseThrow(() -> new BusinessLogicException(AuthException.TOKEN_NOT_FOUND));
        redisTokenInfoRepository.delete(redisTokenInfo);
    }
}
