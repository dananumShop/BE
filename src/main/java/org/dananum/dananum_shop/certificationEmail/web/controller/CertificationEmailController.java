package org.dananum.dananum_shop.certificationEmail.web.controller;

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
public class CertificationEmailController {

    private final CertificationEmailService certificationEmailService;


    @PostMapping("/send-mail")
    public String sendMail(@RequestBody EmailReqDto emailReqDto){
        log.info("이메일 인증 이메일 : {}", emailReqDto.getEmail());
        return certificationEmailService.joinEmail(emailReqDto);
    }

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
