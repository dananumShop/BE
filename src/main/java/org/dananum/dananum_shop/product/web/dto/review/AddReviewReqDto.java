package org.dananum.dananum_shop.product.web.dto.review;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class AddReviewReqDto {

    @Schema(description = "상품 cid")
    private Long productCid;

    @Schema(description = "작성자", example = "test@test.com")
    private String author;

    @Schema(description = "댓글 내용", example = "댓글 내용입니다.")
    private String content;

    @Schema(description = "별점", example = "3")
    private int stars;
}
