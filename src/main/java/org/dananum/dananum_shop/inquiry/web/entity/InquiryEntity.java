package org.dananum.dananum_shop.inquiry.web.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.dananum.dananum_shop.global.web.entity.TimeEntity;
import org.dananum.dananum_shop.global.web.enums.inquiry.InquiryStatus;
import org.dananum.dananum_shop.inquiry.web.dto.add.AddInquiryReqDto;
import org.dananum.dananum_shop.user.web.entity.user.UserEntity;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "inquiry_table")
public class InquiryEntity extends TimeEntity {
    @Id
    @Column(name = "inquiry_cid")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "문의 고유 아이디")
    private Long inquiryCid;

    @NotNull
    @Schema(description = "상품 cid")
    @Column(name = "product_cid")
    private Long productCid;

    @NotNull
    @Schema(description = "작성자")
    @Column(name = "author_cid")
    private Long userCid;

    @NotNull
    @Schema(description = "문의 제목", example = "문의 제목입니다.")
    @Column(name = "inquiry_title")
    private String title;

    @NotNull
    @Schema(description = "문의 내용", example = "문의 내용입니다.")
    @Column(name = "inquiry_content")
    private String content;

    @NotNull
    @Schema(description = "문의 상태", example = "OPEN")
    @Column(name = "inquiry_status")
    private InquiryStatus inquiryStatus;

    public static InquiryEntity from(final UserEntity userEntity, final AddInquiryReqDto addInquiryReqDto) {
        return InquiryEntity.builder()
                .productCid(addInquiryReqDto.getProductCid())
                .userCid(userEntity.getUserCid())
                .title(addInquiryReqDto.getInquiryTitle())
                .content(addInquiryReqDto.getInquiryContent())
                .inquiryStatus(InquiryStatus.OPEN)
                .build();
    }
}
