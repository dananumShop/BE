package org.dananum.dananum_shop.inquiry.web.dto.add;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@ToString
@Slf4j
public class AddInquiryCommentReqDto {

    @Schema(description = "문의 cid")
    private Long inquiryCid;

    @Schema(description = "답변 내용")
    private String commentContent;
}
