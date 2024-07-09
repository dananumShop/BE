package org.dananum.dananum_shop.product.web.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "productdetail_image")
public class ProductDetailImgEntity {
    @Id
    @Column(name = "product_detail_image_cid")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "상품 세부 사진 아이디")
    private Long productDetailImgCid;

    @ManyToOne
    @JoinColumn(name = "product_cid", nullable = false)
    @Schema(description = "상품 cid", example = "1")
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

    public static ProductDetailImgEntity from(final String imageName, final String imagePath, final int imageOrder, final ProductEntity newProduct) {
        return ProductDetailImgEntity.builder()
                .imageName(imageName)
                .imagePath(imagePath)
                .imageOrder(imageOrder)
                .productEntity(newProduct)
                .build();
    }
}
