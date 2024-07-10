package org.dananum.dananum_shop.user.web.dto.getUser;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.dananum.dananum_shop.global.web.dto.CommonResponseDto;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class GetUserInfoResDto {

    @Schema(description = "요청의 성공 상태", example = "true")
    private Boolean success;

    @Schema(description = "요청 코드의 status", example = "200")
    private Integer code;

    @Schema(description = "요청 코드의 에러 메시지", example = "잘못되었습니다")
    private String message;

    @Schema(description = "유저 정보")
    private UserInfoDto userInfo;

    public static GetUserInfoResDto successResponse(final String message, final UserInfoDto userInfo) {
        return GetUserInfoResDto.builder()
                .code(HttpStatus.OK.value())
                .success(true)
                .message(message)
                .userInfo(userInfo)
                .build();
    }
}
