package org.dananum.dananum_shop.inquiry.web.dto.get;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@ToString
@Slf4j
public class GetInquiryListResDto {
    @Schema(description = "요청의 성공 상태", example = "true")
    private Boolean success;

    @Schema(description = "요청 코드의 status", example = "200")
    private Integer code;

    @Schema(description = "요청 코드의 에러 메시지", example = "잘못되었습니다")
    private String message;

    @Schema(description = "문의 리스트")
    private List<GetInquiryDto> getInquiryDtoList;

    public static GetInquiryListResDto successResponse(final String message, List<GetInquiryDto> getInquiryDtoList) {
        return GetInquiryListResDto.builder()
                .code(HttpStatus.OK.value())
                .success(true)
                .message(message)
                .getInquiryDtoList(getInquiryDtoList)
                .build();
    }
}
