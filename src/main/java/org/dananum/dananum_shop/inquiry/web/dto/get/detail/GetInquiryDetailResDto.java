package org.dananum.dananum_shop.inquiry.web.dto.get.detail;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.dananum.dananum_shop.inquiry.web.dto.get.GetInquiryDto;
import org.dananum.dananum_shop.inquiry.web.dto.get.GetInquiryListResDto;
import org.springframework.http.HttpStatus;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@ToString
@Slf4j
public class GetInquiryDetailResDto {
    @Schema(description = "요청의 성공 상태", example = "true")
    private Boolean success;

    @Schema(description = "요청 코드의 status", example = "200")
    private Integer code;

    @Schema(description = "요청 코드의 에러 메시지", example = "잘못되었습니다")
    private String message;

    @Schema(description = "문의 내용")
    private InquiryDetailDto inquiryDetailDto;


    public static GetInquiryDetailResDto successResponse(final String message, InquiryDetailDto inquiryDetailDto) {
        return GetInquiryDetailResDto.builder()
                .code(HttpStatus.OK.value())
                .success(true)
                .message(message)
                .inquiryDetailDto(inquiryDetailDto)
                .build();
    }
}
