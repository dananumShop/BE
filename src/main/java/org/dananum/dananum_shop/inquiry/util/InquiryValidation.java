package org.dananum.dananum_shop.inquiry.util;

import lombok.RequiredArgsConstructor;
import org.dananum.dananum_shop.global.web.advice.exception.CustomAccessDeniedException;
import org.dananum.dananum_shop.global.web.advice.exception.CustomNoSuchElementException;
import org.dananum.dananum_shop.global.web.advice.exception.CustomNotFoundException;
import org.dananum.dananum_shop.global.web.enums.inquiry.InquiryStatus;
import org.dananum.dananum_shop.inquiry.repository.InquiryRepository;
import org.dananum.dananum_shop.inquiry.web.dto.get.GetInquiryDto;
import org.dananum.dananum_shop.inquiry.web.entity.InquiryEntity;
import org.dananum.dananum_shop.user.web.entity.user.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
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
    public InquiryEntity validateInquiryAccess(UserEntity user, Long inquiryCid) {
        InquiryEntity targetInquiry = inquiryRepository.findById(inquiryCid)
                .orElseThrow(() -> new CustomNotFoundException("일치하는 문의가 없습니다."));

        if(!user.getUserCid().equals(targetInquiry.getUserCid())) {
            throw new CustomAccessDeniedException("문의 접근 권한이 없습니다.");
        }

        return targetInquiry;
    }

    /**
     * 사용자가 작성한 문의 목록을 페이징하여 조회하는 메서드입니다.
     *
     * @param page 조회할 페이지 번호 (1부터 시작).
     * @param user 조회하려는 사용자의 엔티티.
     * @return 페이징된 사용자의 문의 목록을 담은 Page 객체.
     * @throws CustomNoSuchElementException 작성된 문의가 없을 경우 발생하는 예외.
     */
    public Page<InquiryEntity> existUserInquiryList(int page, UserEntity user) {
        Pageable pageable = PageRequest.of(page-1, 10);
        Page<InquiryEntity> inquiryList = inquiryRepository.findAllByUserCid(user.getUserCid(), pageable);

        if(inquiryList.isEmpty()) {
            throw new CustomNoSuchElementException("작성된 문의가 없습니다.");
        }

        return inquiryList;
    }

    /**
     * 주어진 페이지 번호에 따라 모든 문의 사항을 가져오는 메서드입니다.
     *
     * @param page 조회할 페이지 번호
     * @param user 조회할 사용자 정보
     * @return 문의 사항 엔티티 페이지
     * @throws CustomNoSuchElementException 문의 사항이 없는 경우
     */
    public Page<InquiryEntity> existInquiryList(int page, UserEntity user) {
        Pageable pageable = PageRequest.of(page-1, 10);
        Page<InquiryEntity> inquiryList = inquiryRepository.findAll(pageable);

        if(inquiryList.isEmpty()) {
            throw new CustomNoSuchElementException("작성된 문의가 없습니다.");
        }

        return inquiryList;
    }

    /**
     * 주어진 페이지 번호와 문의 상태에 따라 문의 사항을 가져오는 메서드입니다.
     *
     * @param page 조회할 페이지 번호
     * @param user 조회할 사용자 정보
     * @param inquiryStatus 조회할 문의 상태
     * @return 문의 사항 엔티티 페이지
     * @throws CustomNoSuchElementException 해당 상태의 문의 사항이 없는 경우
     */
    public Page<InquiryEntity> existInquiryByStatusList(int page, UserEntity user, InquiryStatus inquiryStatus) {
        Pageable pageable = PageRequest.of(page-1, 10);
        Page<InquiryEntity> inquiryList = inquiryRepository.findAllByInquiryStatus(pageable, inquiryStatus);

        if(inquiryList.isEmpty()) {
            throw new CustomNoSuchElementException("상태별 문의가 없습니다.");
        }

        return inquiryList;
    }

    /**
     * InquiryEntity 객체 목록을 GetInquiryDto 객체 목록으로 변환하는 메서드입니다.
     *
     * @param inquiryList InquiryEntity 객체로 구성된 페이지 목록.
     * @return GetInquiryDto 객체로 구성된 목록을 반환합니다.
     */
    public List<GetInquiryDto> inquiryEntityToDto(Page<InquiryEntity> inquiryList) {

        List<GetInquiryDto> getInquiryDtoList = new ArrayList<>();

        for(InquiryEntity inquiry : inquiryList) {
            getInquiryDtoList.add(GetInquiryDto.from(inquiry));
        }

        return getInquiryDtoList;
    }
}
