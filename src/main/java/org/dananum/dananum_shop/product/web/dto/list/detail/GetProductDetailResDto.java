package org.dananum.dananum_shop.product.web.dto.list.detail;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.dananum.dananum_shop.global.web.enums.ProductCategory;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class GetProductDetailResDto {
    @Schema(description = "상품 고유 아이디")
    private Long productCid;

    @Schema(name = "product_name")
    private String productName;

    @Schema(name = "product_category")
    private ProductCategory productCategory;
}
