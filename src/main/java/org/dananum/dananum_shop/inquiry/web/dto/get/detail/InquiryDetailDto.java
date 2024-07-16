package org.dananum.dananum_shop.inquiry.web.dto.get.detail;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.dananum.dananum_shop.global.web.enums.inquiry.InquiryStatus;
import org.dananum.dananum_shop.inquiry.web.entity.InquiryEntity;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@ToString
@Slf4j
public class InquiryDetailDto {

    @Schema(description = "문의 제목")
    private String inquiryTitle;

    @Schema(description = "문의 내용")
    private String inquiryContent;

    @Schema(description = "문의 작성 시간")
    private LocalDateTime createdAt;

    @Schema(description = "문의 상태")
    private InquiryStatus inquiryStatus;

    @Schema(description = "문의 답변 리스트")
    private List<InquiryCommentDto> commentDtoList;

    public static InquiryDetailDto from(InquiryEntity targetInquiry, List<InquiryCommentDto> inquiryCommentDtoList) {
        return InquiryDetailDto.builder()
                .inquiryTitle(targetInquiry.getTitle())
                .inquiryContent(targetInquiry.getContent())
                .createdAt(targetInquiry.getCreatedAt())
                .inquiryStatus(targetInquiry.getInquiryStatus())
                .commentDtoList(inquiryCommentDtoList)
                .build();
    }
}
