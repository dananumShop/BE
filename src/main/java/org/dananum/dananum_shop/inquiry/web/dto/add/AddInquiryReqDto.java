package org.dananum.dananum_shop.inquiry.web.dto.add;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@ToString
@Slf4j
public class AddInquiryReqDto {

    @Schema(description = "상품 cid")
    private Long productCid;

    @Schema(description = "문의 제목")
    private String inquiryTitle;

    @Schema(description = "문의 내용")
    private String inquiryContent;
}
