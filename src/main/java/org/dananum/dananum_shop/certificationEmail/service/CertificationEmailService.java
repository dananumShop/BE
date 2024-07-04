package org.dananum.dananum_shop.certificationEmail.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dananum.dananum_shop.certificationEmail.repository.EmailRepository;
import org.dananum.dananum_shop.certificationEmail.web.dto.EmailReqDto;
import org.dananum.dananum_shop.certificationEmail.web.entity.EmailEntity;
import org.dananum.dananum_shop.global.config.redis.service.EmailService;
import org.dananum.dananum_shop.global.web.advice.exception.CustomDataIntegrityViolationException;
import org.dananum.dananum_shop.global.web.advice.exception.CustomNotFoundException;
import org.dananum.dananum_shop.global.web.enums.EmailCertificationState;
import org.dananum.dananum_shop.user.repository.UserRepository;
import org.dananum.dananum_shop.user.util.UserValidation;
import org.dananum.dananum_shop.user.web.entity.user.UserEntity;
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
    private final EmailRepository emailRepository;
    private final UserRepository userRepository;

    private final UserValidation userValidation;

    private final String verificationCode = generateRandomNumber(100000, 999999);

    @Value("${email.address}")
    private String emailAddress;

    private final long EXPIRE_TIME = 60*5;

    public void joinEmail(EmailReqDto emailReqDto) {

        UserEntity user = userValidation.findUserByUserEmail(emailReqDto.getEmail());

        String title = "[쇼핑몰 인증] 쇼핑몰 가입 인증번호";
        String content =
                "<div style=\"font-family: Arial, sans-serif; font-size: 14px; color: #333;\">" +
                        "<h2 style=\"color: #2E86C1;\">쇼핑몰 가입 인증번호</h2>" +
                        "<p>회원가입 창으로 돌아가 인증 번호를 정확히 입력해주세요.</p>" +
                        "<div style=\"margin: 20px 0; padding: 10px; border: 1px solid #2E86C1; display: inline-block;\">" +
                        "<span style=\"font-size: 24px; font-weight: bold;\">" + verificationCode + "</span>" +
                        "</div>" +
                        "<p>감사합니다!<br>쇼핑몰 팀</p>" +
                        "</div>";

        mailSend(user, title, content);
    }

    @Transactional
    public void mailSend(UserEntity user, String title, String content) {
        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "utf-8");
            helper.setFrom(emailAddress);
            helper.setTo(user.getUserEmail());
            helper.setSubject(title);
            helper.setText(content, true);
            mailSender.send(message);
            emailService.setEmailInfo(user, verificationCode);
        } catch (MessagingException e) {
            log.info(e.getMessage());
        }
    }

    // min과 max 사이의 랜덤한 정수를 반환하는 메서드
    private String generateRandomNumber(int min, int max) {
        Random random = new Random();
        int randomNum = random.nextInt(max - min + 1) + min;

        return Integer.toString(randomNum);
    }

    public void checkAuthNum(String email, String verificationCode) {
        EmailEntity emailEntity = emailRepository.findByUserEmail(email)
                .orElseThrow(() -> new CustomNotFoundException("일치하는 이메일이 없습니다."));

        String storedVerificationCode = emailEntity.getVerificationCode();

        if(storedVerificationCode == null) {
            throw new CustomNotFoundException("인증번호가 없습니다.");
        }

        if(!storedVerificationCode.equals(verificationCode)) {
            throw new CustomDataIntegrityViolationException("인증번호가 일치하지 않습니다.");
        }

        updateUserEmailCertificationState(email);
    }

    private void updateUserEmailCertificationState(String email) {

        UserEntity user = userRepository.findByUserEmail(email)
                .orElseThrow(() -> new CustomNotFoundException("일치하는 유저가 없습니다."));

        user.updateEmailCertificationState(EmailCertificationState.COMPLETED);
        userRepository.save(user);
    }
}
