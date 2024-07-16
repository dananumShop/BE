package org.dananum.dananum_shop.inquiry.web.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import org.dananum.dananum_shop.global.web.entity.TimeEntity;
import org.dananum.dananum_shop.global.web.enums.user.Roles;
import org.dananum.dananum_shop.inquiry.web.dto.add.AddInquiryCommentReqDto;
import org.dananum.dananum_shop.user.web.entity.user.UserEntity;
import org.springframework.security.core.userdetails.User;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "inquiry_comment_table")
public class InquiryCommentEntity extends TimeEntity {
    @Id
    @Column(name = "inquiry_comment_cid")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "문의 답변 고유 아이디")
    private Long inquiryCommentCid;

    @Schema(description = "문의 cid")
    @Column(name = "inquiry_cid")
    private Long inquiryCid;

    @Schema(description = "답변 내용")
    @Column(name = "inquiry_comment_content")
    private String content;

    @Schema(description = "권한")
    @Column(name = "author_role")
    private Roles role;

    public static InquiryCommentEntity from(UserEntity userEntity, AddInquiryCommentReqDto addInquiryCommentReqDto) {
        return InquiryCommentEntity.builder()
                .inquiryCid(addInquiryCommentReqDto.getInquiryCid())
                .content(addInquiryCommentReqDto.getCommentContent())
                .role(userEntity.getUserRole())
                .build();
    }
}
