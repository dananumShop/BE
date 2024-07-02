package org.dananum.dananum_shop.certificationEmail.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dananum.dananum_shop.certificationEmail.web.dto.EmailReqDto;
import org.dananum.dananum_shop.global.config.redis.service.EmailService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Slf4j
public class CertificationEmailService {

    private final JavaMailSender mailSender;
    private final EmailService emailService;

    private final int authNumber = generateRandomNumber(100000, 999999);

    @Value("${email.address}")
    private String emailAddress;

    private final long EXPIRE_TIME = 60*5;

    public String joinEmail(EmailReqDto emailReqDto) {
        String setForm = emailAddress;
        String toMail = emailReqDto.getEmail();
        String title = "[쇼핑몰 인증] 쇼핑몰 가입 인증번호";
        String content =
                "회원가입 창으로 돌아가 인증 번호를 정확히 입력해주세요." +
                        "<br><br>" +
                        "인증 번호는 " + authNumber + "입니다." +
                        "<br>" ;

        mailSend(setForm, toMail, title, content);

        return Integer.toString(authNumber);
    }

    @Transactional
    public void mailSend(String setFrom, String toMail, String title, String content) {
        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "utf-8");
            helper.setFrom(setFrom);
            helper.setTo(toMail);
            helper.setSubject(title);
            helper.setText(content, true);
            mailSender.send(message);
            emailService.setDataExpire(Integer.toString(authNumber), toMail, EXPIRE_TIME);
        } catch (MessagingException e) {
            log.info(e.getMessage());
        }
    }

    // min과 max 사이의 랜덤한 정수를 반환하는 메서드
    public static int generateRandomNumber(int min, int max) {
        Random random = new Random();
        return random.nextInt(max - min + 1) + min;
    }

    public Boolean checkAuthNum(String email, String authNum) {
        if (emailService.getData(authNum) == null) return false;
        else return emailService.getData(authNum).equals(email);
    }
}
