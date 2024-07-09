package org.dananum.dananum_shop.product.web.dto.list.detail;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.dananum.dananum_shop.product.web.dto.list.detail.options.GetDetailImgDto;
import org.dananum.dananum_shop.product.web.dto.list.detail.options.GetInfoImgDto;
import org.dananum.dananum_shop.product.web.dto.list.detail.options.GetOptionDto;
import org.dananum.dananum_shop.product.web.entity.ProductEntity;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class ProductDetailPageDto {
    @Schema(description = "상품 고유 아이디")
    private Long productCid;

    @Schema(description = "상품 이름")
    private String productName;

    @Schema(description = "상품 옵션")
    private List<GetOptionDto> productOptionList;

    @Schema(description = "상품 상세 이미지")
    private List<GetDetailImgDto> productDetailImgList;

    @Schema(description = "상품 상세페에지 이미지")
    private List<GetInfoImgDto> productInfoImgList;

    public static ProductDetailPageDto from(
            ProductEntity targetProduct,
            List<GetOptionDto> optionDtoList,
            List<GetDetailImgDto> detailImageDtoList,
            List<GetInfoImgDto> infoImgDtoList) {

        return ProductDetailPageDto.builder()
                .productCid(targetProduct.getProductCid())
                .productName(targetProduct.getProductName())
                .productOptionList(optionDtoList)
                .productDetailImgList(detailImageDtoList)
                .productInfoImgList(infoImgDtoList)
                .build();
    }
}
