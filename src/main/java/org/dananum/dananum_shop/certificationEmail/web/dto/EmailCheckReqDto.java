package org.dananum.dananum_shop.certificationEmail.web.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class EmailCheckReqDto {
    @Email
    @NotEmpty(message = "이메일을 입력해주세요")
    private String email;

    @NotEmpty(message = "인증 번호를 입력해주세요")
    private String authNum;
}
