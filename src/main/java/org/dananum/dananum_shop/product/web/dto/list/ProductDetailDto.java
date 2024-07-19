package org.dananum.dananum_shop.product.web.dto.list;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.dananum.dananum_shop.product.web.entity.ProductDetailImgEntity;
import org.dananum.dananum_shop.product.web.entity.ProductEntity;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class ProductDetailDto {

    @Schema(description = "상품 고유 아이디", example = "1")
    private Long productCid;

    @Schema(description = "상품 이름", example = "양말")
    private String productName;

    @Schema(description = "상품 썸네일")
    private String productThumbnail;

    @Schema(description = "상품 가격")
    private int price;

    @Schema(description = "상품 설명")
    private String description;

    public static ProductDetailDto from(ProductEntity productEntity, ProductDetailImgEntity productThumbnail) {
        return ProductDetailDto.builder()
                .productCid(productEntity.getProductCid())
                .productName(productEntity.getProductName())
                .productThumbnail(productThumbnail.getImagePath())
                .price(productEntity.getPrice())
                .description(productEntity.getDescription())
                .build();
    }
}
