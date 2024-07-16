package org.dananum.dananum_shop.inquiry.web.dto.get;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.dananum.dananum_shop.global.web.enums.inquiry.InquiryStatus;
import org.dananum.dananum_shop.inquiry.web.entity.InquiryEntity;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@ToString
@Slf4j
public class GetInquiryDto {

    @Schema(description = "문의 고유 번호")
    private Long inquiryCid;

    @Schema(description = "문의 제목")
    private String inquiryTitle;

    @Schema(description = "문의 상태")
    private InquiryStatus inquiryStatus;

    @Schema(description = "작성일")
    private LocalDateTime createdAt;

    public static GetInquiryDto from(InquiryEntity inquiry) {
        return GetInquiryDto.builder()
                .inquiryCid(inquiry.getInquiryCid())
                .inquiryTitle(inquiry.getTitle())
                .inquiryStatus(inquiry.getInquiryStatus())
                .createdAt(inquiry.getCreatedAt())
                .build();
    }
}
