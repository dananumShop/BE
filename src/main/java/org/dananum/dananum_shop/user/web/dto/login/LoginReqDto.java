package org.dananum.dananum_shop.user.web.dto.login;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class LoginReqDto {

    @Schema(description = "유저 이메일", example = "test@test.test")
    private String userEmail;

    @Schema(description = "비밀번호", example = "test")
    private String userPassword;

    public UsernamePasswordAuthenticationToken toAuthentication() {
        return new UsernamePasswordAuthenticationToken(userEmail, userPassword);
    }
}
