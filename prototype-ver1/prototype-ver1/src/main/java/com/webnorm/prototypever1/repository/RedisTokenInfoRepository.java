package com.webnorm.prototypever1.repository;

import com.webnorm.prototypever1.security.redis.RedisTokenInfo;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RedisTokenInfoRepository extends CrudRepository<RedisTokenInfo, String> {
    Optional<RedisTokenInfo> findByAccessToken(String accessToken);

    Optional<RedisTokenInfo> findByRefreshToken(String refreshToken);
}
