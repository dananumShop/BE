package org.dananum.dananum_shop.product.web.dto.list.detail.options;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import lombok.*;
import org.dananum.dananum_shop.product.web.entity.ProductDetailImgEntity;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class GetDetailImgDto {

    @Schema(description = "이미지 링크")
    private String detailImgPath;

    @Schema(description = "이미지 순서")
    private int order;

    public static GetDetailImgDto from(ProductDetailImgEntity detailImg) {
        return GetDetailImgDto.builder()
                .detailImgPath(detailImg.getImagePath())
                .order(detailImg.getImageOrder())
                .build();
    }
}
