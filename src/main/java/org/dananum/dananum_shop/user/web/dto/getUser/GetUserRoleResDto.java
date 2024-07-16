package org.dananum.dananum_shop.user.web.dto.getUser;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.dananum.dananum_shop.global.web.enums.user.Roles;
import org.dananum.dananum_shop.user.web.entity.user.UserEntity;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class GetUserRoleResDto {

    @Schema(description = "유저 권한 정보", example = "ROLE_USER")
    private Roles role;

    public static GetUserRoleResDto from(final UserEntity user) {
        return GetUserRoleResDto.builder()
                .role(user.getUserRole())
                .build();
    }
}
