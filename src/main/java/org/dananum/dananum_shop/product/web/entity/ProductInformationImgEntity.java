package org.dananum.dananum_shop.product.web.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.dananum.dananum_shop.global.web.entity.TimeEntity;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "product_information_img_table")
public class ProductInformationImgEntity extends TimeEntity {

    @Id
    @Column(name = "product_information_img_cid")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "상품 상세 이미지 고유 아이디")
    private Long productInformationImgCid;

    @ManyToOne
    @JoinColumn(name = "product_cid", nullable = false)
    @NotNull
    @Schema(description = "상품 cid", example = "1")
    @Column(name = "product_category")
    private ProductEntity productEntity;

    @NotNull
    @Schema(description = "이미지 URL")
    @Column(name = "image_path")
    private String imagePath;

    @NotNull
    @Schema(description = "이미지 이름")
    @Column(name = "image_name")
    private String imageName;

    @NotNull
    @Schema(description = "이미지 순서")
    @Column(name = "image_order")
    private int imageOrder;
}
