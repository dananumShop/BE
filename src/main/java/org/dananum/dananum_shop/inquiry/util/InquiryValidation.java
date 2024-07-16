package org.dananum.dananum_shop.inquiry.util;

import lombok.RequiredArgsConstructor;
import org.dananum.dananum_shop.global.web.advice.exception.CustomAccessDeniedException;
import org.dananum.dananum_shop.global.web.advice.exception.CustomNoSuchElementException;
import org.dananum.dananum_shop.global.web.advice.exception.CustomNotFoundException;
import org.dananum.dananum_shop.inquiry.repository.InquiryRepository;
import org.dananum.dananum_shop.inquiry.web.entity.InquiryEntity;
import org.dananum.dananum_shop.user.web.entity.user.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class InquiryValidation {

    private final InquiryRepository inquiryRepository;

    /**
     * 사용자가 특정 문의에 접근할 수 있는 권한이 있는지 확인합니다.
     *
     * @param user 접근하려는 사용자의 엔티티.
     * @param inquiryCid 접근하려는 문의의 고유 ID.
     * @throws CustomNotFoundException 해당 ID의 문의가 존재하지 않을 경우.
     * @throws CustomAccessDeniedException 사용자가 해당 문의에 접근 권한이 없을 경우.
     */
    public void validateInquiryAccess(UserEntity user, Long inquiryCid) {
        InquiryEntity targetInquiry = inquiryRepository.findById(inquiryCid)
                .orElseThrow(() -> new CustomNotFoundException("일치하는 문의가 없습니다."));

        if(!user.getUserCid().equals(targetInquiry.getUserCid())) {
            throw new CustomAccessDeniedException("문의 접근 권한이 없습니다.");
        }
    }

    public Page<InquiryEntity> existInquiryList(int page, UserEntity user) {
        Pageable pageable = PageRequest.of(page-1, 10);
        Page<InquiryEntity> inquiryList = inquiryRepository.findAllByUserCid(user.getUserCid(), pageable);

        if(inquiryList.isEmpty()) {
            throw new CustomNoSuchElementException("작성된 문의가 없습니다.");
        }

        return inquiryList;
    }
}
