package org.dananum.dananum_shop.user.web.dto.custom;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.dananum.dananum_shop.user.web.entity.user.UserEntity;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class TokenUserDto {

    @Schema(description = "유저 이메일", example = "test@test.test")
    private String userEmail;

    @Schema(description = "비밀번호", example = "test")
    private String userPassword;

    @Schema(description = "유저권한", example = "ROLE_USER")
    private String roles;

    public static TokenUserDto toDto(UserEntity userEntity){
        return TokenUserDto.builder()
                .userEmail(userEntity.getUserEmail())
                .userPassword(userEntity.getUserPassword())
                .roles(userEntity.getUserRole().getType())
                .build();
    }
}
