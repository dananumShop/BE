package org.dananum.dananum_shop.inquiry.web.dto.get.detail;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.dananum.dananum_shop.global.web.enums.user.Roles;
import org.dananum.dananum_shop.inquiry.web.entity.InquiryCommentEntity;
import org.dananum.dananum_shop.user.web.entity.user.UserEntity;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@ToString
@Slf4j
public class InquiryCommentDto {
    @Schema(description = "답변 내용")
    private String content;

    @Schema(description = "권한")
    private Roles role;

    @Schema(description = "작성 시간")
    private LocalDateTime createdAt;

    public static InquiryCommentDto from(InquiryCommentEntity comment, UserEntity user) {
        return InquiryCommentDto.builder()
                .content(comment.getContent())
                .role(user.getUserRole())
                .createdAt(comment.getCreatedAt())
                .build();
    }
}
