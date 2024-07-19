package org.dananum.dananum_shop.productReview.web.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.dananum.dananum_shop.global.web.entity.TimeEntity;
import org.dananum.dananum_shop.product.web.entity.ProductEntity;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "product_review_table")
public class ProductReviewEntity extends TimeEntity {
    @Id
    @Column(name = "product_review_cid")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "상품 리뷰 고유 아이디")
    private Long productReviewCid;

    @ManyToOne
    @JoinColumn(name = "product_cid", nullable = false)
    @Schema(description = "상품 cid", example = "1")
    private ProductEntity productEntity;

    @NotNull
    @Schema(description = "작성자", example = "tes***")
    @Column(name = "review_author")
    private String author;

    @NotNull
    @Schema(description = "리뷰 내용", example = "리뷰 내용입니다.")
    @Column(name = "review_content")
    private String content;

    @NotNull
    @Schema(description = "별점", example = "3")
    @Column(name = "review_stars")
    private int stars;
}
