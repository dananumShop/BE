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
@Table(name = "product_Thumbnail")
public class ProductThumbnailEntity extends TimeEntity {
    @Id
    @Column(name = "product_cid")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "상품 썸네일 아이디")
    private Long productThumbnailCid;

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

    public static ProductThumbnailEntity from(final String imageName, final String imagePath, final ProductEntity newProduct) {
        return ProductThumbnailEntity.builder()
                .imageName(imageName)
                .imagePath(imagePath)
                .productEntity(newProduct)
                .build();
    }
}
