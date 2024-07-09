package org.dananum.dananum_shop.product.web.dto.list.detail;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class GetProductDetailResDto {

    @Schema(description = "요청의 성공 상태", example = "true")
    private Boolean success;

    @Schema(description = "요청 코드의 status", example = "200")
    private Integer code;

    @Schema(description = "요청 코드의 에러 메시지", example = "잘못되었습니다")
    private String message;

    @Schema(description = "상품 상세 정보")
    private ProductDetailPageDto productDetailPageDto;

    public static GetProductDetailResDto successResponse(final String message, final ProductDetailPageDto productDetailPageDto) {
        return GetProductDetailResDto.builder()
                .success(true)
                .code(200)
                .message(message)
                .productDetailPageDto(productDetailPageDto)
                .build();
    }
}
