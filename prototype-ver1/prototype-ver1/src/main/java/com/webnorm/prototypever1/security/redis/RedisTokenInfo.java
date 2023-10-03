package com.webnorm.prototypever1.security.redis;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import org.springframework.data.redis.core.index.Indexed;

import java.io.Serializable;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@RedisHash(value = "redisTokenInfo")
public class RedisTokenInfo implements Serializable {

    @Id
    private String id;

    @Indexed
    private String accessToken;
    @Indexed
    private String refreshToken;
    @TimeToLive
    private long expiredTime = 12 * 60 * 60;    // 만료기한 12시간으로 설정

    @Builder
    public RedisTokenInfo(String id, String accessToken, String refreshToken) {
        this.id = id;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public RedisTokenInfo update(String accessToken, String refreshToken) {
        this.refreshToken = refreshToken;
        this.accessToken = accessToken;
        return this;
    }
}
