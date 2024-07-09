package org.dananum.dananum_shop.product.web.dto.list.detail.options;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.dananum.dananum_shop.product.web.entity.ProductInformationImgEntity;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class GetInfoImgDto {

    @Schema(description = "이미지 링크")
    private String detailImgPath;

    @Schema(description = "이미지 순서")
    private int order;

    public static GetInfoImgDto from(ProductInformationImgEntity infoImg) {
        return GetInfoImgDto.builder()
                .detailImgPath(infoImg.getImagePath())
                .order(infoImg.getImageOrder())
                .build();
    }
}
