package org.dananum.dananum_shop.user.web.dto.getUser;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.dananum.dananum_shop.user.web.entity.user.UserEntity;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class UserInfoDto {
    @Schema(description = "유저 고유 아이디")
    private Long userCid;

    @Schema(description = "유저 이메일(아이디)", example = "test@test.test")
    private String userEmail;

    @Schema(description = "유저 이름", example = "홍길동")
    private String userName;

    public static UserInfoDto from(final UserEntity userEntity) {
        return UserInfoDto.builder()
                .userCid(userEntity.getUserCid())
                .userEmail(userEntity.getUserEmail())
                .userName(userEntity.getUserName())
                .build();
    }
}
