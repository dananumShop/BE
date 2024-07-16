package org.dananum.dananum_shop.product.web.dto.crud;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.dananum.dananum_shop.global.web.enums.product.ProductGender;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@ToString
public class AddProductReqDto {

    @Schema(description = "상품 이름", example = "양말")
    private String productName;

    @Schema(description = "상품 카테고리", example = "CLOTH")
    private String productCategory;

    @Schema(description = "상품 성별", example = "MALE")
    private ProductGender productGender;
}
