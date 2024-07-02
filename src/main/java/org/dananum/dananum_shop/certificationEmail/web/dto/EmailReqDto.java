package org.dananum.dananum_shop.certificationEmail.web.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmailReqDto {
    @Email(message = "이메일 형식을 확인해 주세요")
    @NotEmpty(message = "이메일을 입력해주세요.")
    private String email;
}
