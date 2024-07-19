package org.dananum.dananum_shop.product.web.dto.crud;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;
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

    @Schema(description = "상품 가격")
    private int price;

    @Schema(description = "상품 설명")
    private String description;
}
