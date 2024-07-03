package org.dananum.dananum_shop.certificationEmail.web.entity;

import jakarta.persistence.Id;
import lombok.*;
import org.dananum.dananum_shop.user.web.entity.user.UserEntity;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

@RedisHash(value = "email_certification", timeToLive = 60 * 3)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Builder
public class EmailEntity {

    @Id
    private String id;

    private String verificationCode;

    @Indexed
    private String userEmail;

    public static EmailEntity from(UserEntity user, String verificationCode) {
        return EmailEntity.builder()
                .id(user.getUserCid().toString())
                .userEmail(user.getUserEmail())
                .verificationCode(verificationCode)
                .build();
    }
}
