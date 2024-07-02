package org.dananum.dananum_shop.global.config.redis.entity;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

@RedisHash(value = "refresh_token", timeToLive = 60*60*24)
@AllArgsConstructor
@Getter
public class RefreshToken {

    @Id
    private String id;

    private String refreshToken;

    @Indexed
    private String accessToken;
}
