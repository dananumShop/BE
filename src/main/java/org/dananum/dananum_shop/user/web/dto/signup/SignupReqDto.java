package org.dananum.dananum_shop.user.web.dto.signup;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class SignupReqDto {

    @Schema(description = "유저 이메일", example = "test@test.test")
    private String userEmail;

    @Schema(description = "유저 이름", example = "테스터")
    private String userName;

    @Schema(description = "유저 닉네임", example = "test")
    private String userNickname;

    @Schema(description = "비밀번호", example = "test")
    private String userPassword;
}
