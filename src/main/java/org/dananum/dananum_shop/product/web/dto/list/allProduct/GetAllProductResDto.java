package org.dananum.dananum_shop.product.web.dto.list.allProduct;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.dananum.dananum_shop.product.web.dto.list.ProductDetailDto;
import org.springframework.http.HttpStatus;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class GetAllProductResDto {

    @Schema(description = "요청의 성공 상태", example = "true")
    private Boolean success;

    @Schema(description = "요청 코드의 status", example = "200")
    private Integer code;

    @Schema(description = "요청 코드의 에러 메시지", example = "잘못되었습니다")
    private String message;

    @Schema(description = "상품 상세 내용")
    List<ProductDetailDto> productDetailDtoList;

    public static GetAllProductResDto successResponse(String message, List<ProductDetailDto> productDetailDtoList) {
        return GetAllProductResDto.builder()
                .productDetailDtoList(productDetailDtoList)
                .message(message)
                .code(HttpStatus.OK.value())
                .success(true)
                .build();
    }
}
