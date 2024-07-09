package org.dananum.dananum_shop.certificationEmail.web.entity;

import jakarta.persistence.Id;
import lombok.*;
import org.dananum.dananum_shop.certificationEmail.repository.EmailRepository;
import org.dananum.dananum_shop.certificationEmail.web.dto.EmailReqDto;
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


    public static EmailEntity from(EmailReqDto emailReqDto, String verificationCode) {
        return EmailEntity.builder()
                .id(emailReqDto.getEmail())
                .verificationCode(verificationCode)
                .build();
    }
}
