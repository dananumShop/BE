package org.dananum.dananum_shop.inquiry.service;

import lombok.RequiredArgsConstructor;
import org.dananum.dananum_shop.global.util.AdminValidation;
import org.dananum.dananum_shop.global.web.advice.exception.CustomNotFoundException;
import org.dananum.dananum_shop.global.web.enums.inquiry.InquiryStatus;
import org.dananum.dananum_shop.inquiry.repository.InquiryCommentRepository;
import org.dananum.dananum_shop.inquiry.repository.InquiryRepository;
import org.dananum.dananum_shop.inquiry.util.InquiryValidation;
import org.dananum.dananum_shop.inquiry.web.dto.add.AddInquiryCommentReqDto;
import org.dananum.dananum_shop.inquiry.web.dto.get.GetInquiryDto;
import org.dananum.dananum_shop.inquiry.web.dto.get.detail.InquiryCommentDto;
import org.dananum.dananum_shop.inquiry.web.dto.get.detail.InquiryDetailDto;
import org.dananum.dananum_shop.inquiry.web.entity.InquiryCommentEntity;
import org.dananum.dananum_shop.inquiry.web.entity.InquiryEntity;
import org.dananum.dananum_shop.user.web.entity.user.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminInquiryService {

    private final InquiryRepository inquiryRepository;
    private final InquiryCommentRepository inquiryCommentRepository;

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
    @Transactional(readOnly = true)
    public List<GetInquiryDto> getAllInquiry(User user, int page) {
        UserEntity userEntity = adminValidation.validateAdminUser(user);

        Page<InquiryEntity> inquiryList = inquiryValidation.existInquiryList(page, userEntity);

        return inquiryValidation.inquiryEntityToDto(inquiryList);
    }

    /**
     * 주어진 사용자, 페이지 번호 및 문의 상태에 따라 문의 사항을 가져오는 메서드입니다.
     *
     * @param user 조회할 사용자 정보
     * @param page 조회할 페이지 번호
     * @param inquiryStatus 조회할 문의 상태
     * @return 문의 사항 DTO 목록
     * @throws AdminValidationException 사용자가 관리자 권한이 없는 경우
     * @throws InquiryNotFoundException 해당 페이지에 문의 사항이 없는 경우
     */
    @Transactional(readOnly = true)
    public List<GetInquiryDto> getInquiryByStatus(User user, int page, InquiryStatus inquiryStatus) {
        UserEntity userEntity = adminValidation.validateAdminUser(user);

        Page<InquiryEntity> inquiryList = inquiryValidation.existInquiryByStatusList(page, userEntity, inquiryStatus);

        return inquiryValidation.inquiryEntityToDto(inquiryList);
    }

    /**
     * 주어진 사용자와 요청 DTO에 따라 새로운 문의 댓글을 추가하는 메서드입니다.
     *
     * @param user 댓글을 추가할 사용자 정보
     * @param addInquiryCommentReqDto 추가할 댓글의 정보를 담고 있는 요청 DTO
     * @throws AdminValidationException 사용자가 관리자 권한이 없는 경우
     */
    @Transactional
    public void addInquiryComment(User user, AddInquiryCommentReqDto addInquiryCommentReqDto) {
        UserEntity userEntity = adminValidation.validateAdminUser(user);

        InquiryCommentEntity newComment = InquiryCommentEntity.from(userEntity, addInquiryCommentReqDto);

        inquiryCommentRepository.save(newComment);
    }

    public InquiryDetailDto getInquiryDetail(User user, Long inquiryCid) {
        UserEntity userEntity = adminValidation.validateAdminUser(user);

        InquiryEntity targetInquiry = inquiryRepository.findById(inquiryCid)
                .orElseThrow(() -> new CustomNotFoundException("일치하는 문의가 없습니다."));

        List<InquiryCommentDto> inquiryCommentDtoList = commentList(targetInquiry, userEntity);

        return InquiryDetailDto.from(targetInquiry, inquiryCommentDtoList);
    }

    /**
     * 특정 문의에 달린 댓글 목록을 가져오는 메서드입니다.
     *
     * @param targetInquiry 댓글을 조회할 문의 엔티티.
     * @param user 댓글을 조회하는 사용자의 엔티티.
     * @return 조회된 댓글 목록을 담은 InquiryCommentDto 객체 리스트.
     */
    private List<InquiryCommentDto> commentList(InquiryEntity targetInquiry, UserEntity user) {
        List<InquiryCommentEntity> inquiryCommentEntityList = inquiryCommentRepository.findAllByInquiryCid(targetInquiry.getInquiryCid());

        return commentEntityToDto(inquiryCommentEntityList, user);
    }

    /**
     * 댓글 엔티티 목록을 InquiryCommentDto 객체 목록으로 변환하는 메서드입니다.
     *
     * @param inquiryCommentEntityList 댓글 엔티티 객체 목록.
     * @param user 댓글을 조회하는 사용자의 엔티티.
     * @return InquiryCommentDto 객체로 변환된 댓글 목록.
     */
    private List<InquiryCommentDto> commentEntityToDto(List<InquiryCommentEntity> inquiryCommentEntityList, UserEntity user) {
        List<InquiryCommentDto> inquiryCommentDtoList = new ArrayList<>();

        for(InquiryCommentEntity comment : inquiryCommentEntityList) {
            inquiryCommentDtoList.add(InquiryCommentDto.from(comment, user));
        }

        return inquiryCommentDtoList;
    }

    /**
     * 주어진 사용자와 문의 ID에 따라 문의 상태를 업데이트하는 메서드입니다.
     *
     * @param user 문의 상태를 업데이트할 사용자 정보
     * @param inquiryCid 상태를 업데이트할 문의 ID
     * @throws AdminValidationException 사용자가 관리자 권한이 없는 경우
     * @throws CustomNotFoundException 일치하는 문의를 찾을 수 없는 경우
     */
    @Transactional
    public void updateInquiryStatus(User user, Long inquiryCid) {
        adminValidation.validateAdminUser(user);

        InquiryEntity targetInquiry = inquiryRepository.findById(inquiryCid)
                .orElseThrow(() -> new CustomNotFoundException("일치하는 문의가 없습니다."));

        targetInquiry.updateInquiryStatus(targetInquiry.getInquiryStatus());

        inquiryRepository.save(targetInquiry);
    }

    /**
     * 주어진 사용자와 문의 ID에 따라 문의를 삭제하는 메서드입니다.
     *
     * @param user 문의를 삭제할 사용자 정보
     * @param inquiryCid 삭제할 문의 ID
     * @throws AdminValidationException 사용자가 관리자 권한이 없는 경우
     * @throws CustomNotFoundException 일치하는 문의를 찾을 수 없는 경우
     */
    @Transactional
    public void deleteInquiry(User user, Long inquiryCid) {
        adminValidation.validateAdminUser(user);

        InquiryEntity targetInquiry = inquiryRepository.findById(inquiryCid)
                .orElseThrow(() -> new CustomNotFoundException("일치하는 문의가 없습니다."));

        inquiryRepository.delete(targetInquiry);
    }
}
