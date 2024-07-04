package org.dananum.dananum_shop.user.web.dto.getUser;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.dananum.dananum_shop.global.web.enums.EmailCertificationState;
import org.dananum.dananum_shop.global.web.enums.Roles;
import org.dananum.dananum_shop.user.web.entity.user.UserEntity;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class GetUserVerificationDto {

    @Schema(description = "유저 이메일 인증 상태", example = "NEEDED")
    private EmailCertificationState emailCertificationState;

    @Schema(description = "유저 권한 정보", example = "ROLE_USER")
    private Roles role;

    public static GetUserVerificationDto from(final UserEntity user) {
        return GetUserVerificationDto.builder()
                .emailCertificationState(user.getEmailCertificationState())
                .role(user.getUserRole())
                .build();
    }
}
