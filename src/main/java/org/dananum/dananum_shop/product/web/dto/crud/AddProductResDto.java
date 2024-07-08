package org.dananum.dananum_shop.product.web.dto.crud;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.dananum.dananum_shop.global.web.dto.CommonResponseDto;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@ToString
@Slf4j
public class AddProductResDto {

    @Schema(description = "요청의 성공 상태", example = "true")
    private Boolean success;

    @Schema(description = "요청 코드의 status", example = "200")
    private Integer code;

    @Schema(description = "요청 코드의 에러 메시지", example = "잘못되었습니다")
    private String message;

    @Schema(description = "생성된 product_cid", example = "1")
    private Long productCid;

    public static AddProductResDto createSuccessResponse(final String message, final Long productCid){
        return AddProductResDto.builder()
                .code(HttpStatus.CREATED.value())
                .success(true)
                .message(message)
                .productCid(productCid)
                .build();
    }
}
