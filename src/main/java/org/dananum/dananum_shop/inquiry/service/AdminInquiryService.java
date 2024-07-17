package org.dananum.dananum_shop.inquiry.service;

import lombok.RequiredArgsConstructor;
import org.dananum.dananum_shop.global.util.AdminValidation;
import org.dananum.dananum_shop.inquiry.repository.InquiryRepository;
import org.dananum.dananum_shop.inquiry.util.InquiryValidation;
import org.dananum.dananum_shop.inquiry.web.dto.get.GetInquiryDto;
import org.dananum.dananum_shop.inquiry.web.entity.InquiryEntity;
import org.dananum.dananum_shop.user.web.entity.user.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminInquiryService {

    private final InquiryRepository inquiryRepository;

    private final AdminValidation adminValidation;
    private final InquiryValidation inquiryValidation;

    /**
     * 주어진 사용자와 페이지 번호에 따라 모든 문의 사항을 가져오는 메서드입니다.
     *
     * @param user 문의 사항을 조회할 사용자 정보
     * @param page 조회할 페이지 번호
     * @return 문의 사항 DTO 목록
     * @throws AdminValidationException 사용자가 관리자 권한이 없는 경우
     * @throws InquiryNotFoundException 해당 페이지에 문의 사항이 없는 경우
     */
    public List<GetInquiryDto> getAllInquiry(User user, int page) {
        UserEntity userEntity = adminValidation.validateAdminUser(user);

        Page<InquiryEntity> inquiryList = inquiryValidation.existInquiryList(page, userEntity);

        return inquiryValidation.inquiryEntityToDto(inquiryList);
    }
}
