package org.dananum.dananum_shop.inquiry.service;

import lombok.RequiredArgsConstructor;
import org.dananum.dananum_shop.inquiry.repository.InquiryCommentRepository;
import org.dananum.dananum_shop.inquiry.repository.InquiryRepository;
import org.dananum.dananum_shop.inquiry.util.InquiryValidation;
import org.dananum.dananum_shop.inquiry.web.dto.add.AddInquiryCommentReqDto;
import org.dananum.dananum_shop.inquiry.web.dto.add.AddInquiryReqDto;
import org.dananum.dananum_shop.inquiry.web.dto.get.GetInquiryDto;
import org.dananum.dananum_shop.inquiry.web.entity.InquiryCommentEntity;
import org.dananum.dananum_shop.inquiry.web.entity.InquiryEntity;
import org.dananum.dananum_shop.user.util.UserValidation;
import org.dananum.dananum_shop.user.web.entity.user.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InquiryService {

    private final InquiryRepository inquiryRepository;
    private final InquiryCommentRepository inquiryCommentRepository;

    private final UserValidation userValidation;
    private final InquiryValidation inquiryValidation;

    /**
     * 새로운 문의를 시스템에 추가합니다.
     *
     * @param user 문의를 작성하는 사용자.
     * @param addInquiryReqDto 문의 세부 정보를 포함하는 데이터 전송 객체.
     * @throws UserNotFoundException 사용자가 존재하지 않을 경우.
     */
    @Transactional
    public void addInquiry(User user, AddInquiryReqDto addInquiryReqDto) {
        UserEntity userEntity = userValidation.validateExistUser(user.getUsername());

        InquiryEntity newInquiry = InquiryEntity.from(userEntity, addInquiryReqDto);

        inquiryRepository.save(newInquiry);
    }

    /**
     * 기존 문의에 새로운 댓글을 추가합니다.
     *
     * @param user 댓글을 작성하는 사용자.
     * @param addInquiryCommentReqDto 댓글 세부 정보를 포함하는 데이터 전송 객체.
     * @throws UserNotFoundException 사용자가 존재하지 않을 경우.
     * @throws CustomAccessDeniedException 사용자가 해당 문의에 접근 권한이 없을 경우.
     * @throws CustomNotFoundException 해당 문의가 존재하지 않을 경우.
     */
    @Transactional
    public void addInquiryComment(User user, AddInquiryCommentReqDto addInquiryCommentReqDto) {
        UserEntity userEntity = userValidation.validateExistUser(user.getUsername());

        inquiryValidation.validateInquiryAccess(userEntity, addInquiryCommentReqDto.getInquiryCid());

        InquiryCommentEntity newComment = InquiryCommentEntity.from(userEntity, addInquiryCommentReqDto);

        inquiryCommentRepository.save(newComment);
    }

    /**
     * 사용자의 문의 목록을 가져오는 메서드입니다.
     *
     * @param user 조회하려는 사용자 정보.
     * @param page 가져올 페이지 번호.
     * @return 조회된 문의 목록을 GetInquiryDto로 변환하여 반환합니다.
     * @throws UserNotFoundException 사용자가 존재하지 않을 경우.
     */
    @Transactional(readOnly = true)
    public List<GetInquiryDto> getInquiryList(User user, int page) {
        UserEntity userEntity = userValidation.validateExistUser(user.getUsername());

        Page<InquiryEntity> inquiryList = inquiryValidation.existInquiryList(page, userEntity);

        return InquiryEntityToDto(inquiryList);
    }

    /**
     * InquiryEntity 객체 목록을 GetInquiryDto 객체 목록으로 변환하는 메서드입니다.
     *
     * @param inquiryList InquiryEntity 객체로 구성된 페이지 목록.
     * @return GetInquiryDto 객체로 구성된 목록을 반환합니다.
     */
    private List<GetInquiryDto> InquiryEntityToDto(Page<InquiryEntity> inquiryList) {

        List<GetInquiryDto> getInquiryDtoList = new ArrayList<>();

        for(InquiryEntity inquiry : inquiryList) {
            getInquiryDtoList.add(GetInquiryDto.from(inquiry));
        }

        return getInquiryDtoList;
    }
}
