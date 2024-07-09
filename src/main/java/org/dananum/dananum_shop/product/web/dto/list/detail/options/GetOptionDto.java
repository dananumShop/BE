package org.dananum.dananum_shop.product.web.dto.list.detail.options;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.dananum.dananum_shop.global.web.enums.ProductOptionSize;
import org.dananum.dananum_shop.product.web.entity.ProductOptionEntity;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class GetOptionDto {

    @Schema(description = "옵션 고유 아이디")
    private Long productOptionCid;

    @Schema(description = "옵션 사이즈", example = "L")
    private ProductOptionSize size;

    @Schema(description = "옵션 재고", example = "25")
    private int stock;

    public static GetOptionDto from(ProductOptionEntity productOptionEntity) {
        return GetOptionDto.builder()
                .productOptionCid(productOptionEntity.getProductOptionCid())
                .size(productOptionEntity.getSize())
                .stock(productOptionEntity.getStock())
                .build();
    }
}
