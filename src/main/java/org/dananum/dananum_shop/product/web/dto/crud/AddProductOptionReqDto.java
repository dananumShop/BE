package org.dananum.dananum_shop.product.web.dto.crud;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.dananum.dananum_shop.global.web.enums.product.ProductOptionSize;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class AddProductOptionReqDto {

    @Schema(description = "옵션 사이즈", example = "L")
    private ProductOptionSize size;

    @Schema(description = "옵션 재고", example = "24")
    private int stock;
}
