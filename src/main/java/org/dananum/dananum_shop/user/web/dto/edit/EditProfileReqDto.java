package org.dananum.dananum_shop.user.web.dto.edit;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class EditProfileReqDto {
    @Schema(description = "유저 이름", example = "홍길동")
    private String userName;

    @Schema(description = "유저 비밀번호", example = "test")
    private String userPassword;
}
