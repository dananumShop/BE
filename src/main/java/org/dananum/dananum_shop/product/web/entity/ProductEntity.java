package org.dananum.dananum_shop.product.web.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.dananum.dananum_shop.global.web.entity.TimeEntity;
import org.dananum.dananum_shop.global.web.enums.ProductCategory;
import org.dananum.dananum_shop.product.web.dto.crud.AddProductReqDto;

import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "product_table")
public class ProductEntity extends TimeEntity {
    @Id
    @Column(name = "product_cid")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "상품 고유 아이디")
    private Long productCid;

    @NotNull
    @Schema(description = "상품 이름", example = "양말")
    @Column(name = "product_name")
    private String productName;

    @NotNull
    @Schema(description = "상품 카테고리", example = "cloth")
    @Column(name = "product_category")
    private ProductCategory productCategory;

    public static ProductEntity from(final AddProductReqDto addProductReqDto) {
        return ProductEntity.builder()
                .productName(addProductReqDto.getProductName())
                .productCategory(addProductReqDto.getProductCategory())
                .build();
    }
}
