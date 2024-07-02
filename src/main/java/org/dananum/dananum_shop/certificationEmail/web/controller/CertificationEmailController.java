package org.dananum.dananum_shop.certificationEmail.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dananum.dananum_shop.certificationEmail.web.dto.EmailCheckReqDto;
import org.dananum.dananum_shop.certificationEmail.web.dto.EmailReqDto;
import org.dananum.dananum_shop.certificationEmail.web.service.CertificationEmailService;
import org.dananum.dananum_shop.global.web.advice.exception.CustomDataIntegerityCiolationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/public/mail")
@Tag(name = "회원관련 API")
public class CertificationEmailController {

    private final CertificationEmailService certificationEmailService;

    @Operation(summary = "이메일 요청", description = "이메일 인증에 필요한 인증번호를 받는 api입니다.")
    @PostMapping("/send-mail")
    public String sendMail(@RequestBody EmailReqDto emailReqDto){
        log.info("이메일 인증 이메일 : {}", emailReqDto.getEmail());
        return certificationEmailService.joinEmail(emailReqDto);
    }

    @Operation(summary = "이메일 확인", description = "이메일로 받은 인증번호를 확인하는 api입니다.")
    @PostMapping("/check-auth-num")
    public String checkAuthNum(@RequestBody EmailCheckReqDto emailCheckReqDto) {
        Boolean checked = certificationEmailService.checkAuthNum(emailCheckReqDto.getEmail(), emailCheckReqDto.getAuthNum());
        if (checked) {
            return "OK";
        }else {
            throw new CustomDataIntegerityCiolationException("인증번호가 잘못되었습니다.");
        }
    }
}
