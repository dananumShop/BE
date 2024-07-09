package org.dananum.dananum_shop.global.config.redis.service;

import lombok.RequiredArgsConstructor;
import org.dananum.dananum_shop.certificationEmail.repository.EmailRepository;
import org.dananum.dananum_shop.certificationEmail.web.dto.EmailReqDto;
import org.dananum.dananum_shop.certificationEmail.web.entity.EmailEntity;
import org.dananum.dananum_shop.user.web.entity.user.UserEntity;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class EmailService {

    private final EmailRepository emailRepository;

    public void setEmailInfo(EmailReqDto emailReqDto, String verificationCode) {
        emailRepository.save(EmailEntity.from(emailReqDto, verificationCode));
    }

}
