package org.dananum.dananum_shop.inquiry.service;

import lombok.RequiredArgsConstructor;
import org.dananum.dananum_shop.inquiry.repository.InquiryRepository;
import org.dananum.dananum_shop.inquiry.web.dto.add.AddInquiryReqDto;
import org.dananum.dananum_shop.inquiry.web.entity.InquiryEntity;
import org.dananum.dananum_shop.user.util.UserValidation;
import org.dananum.dananum_shop.user.web.entity.user.UserEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InquiryService {

    private final InquiryRepository inquiryRepository;

    private final UserValidation userValidation;

    public void addInquiry(User user, AddInquiryReqDto addInquiryReqDto) {
        UserEntity userEntity = userValidation.validateExistUser(user.getUsername());

        InquiryEntity newInquiry = InquiryEntity.from(userEntity, addInquiryReqDto);

        inquiryRepository.save(newInquiry);
    }
}
