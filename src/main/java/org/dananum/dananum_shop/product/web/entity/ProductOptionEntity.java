package org.dananum.dananum_shop.product.web.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.dananum.dananum_shop.global.web.entity.TimeEntity;
import org.dananum.dananum_shop.global.web.enums.ProductCategory;
import org.dananum.dananum_shop.global.web.enums.ProductOptionSize;
import org.dananum.dananum_shop.product.web.dto.crud.AddProductOptionReqDto;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "product_option_table")
public class ProductOptionEntity extends TimeEntity {

    @Id
    @Column(name = "product_option_cid")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "상품옵션 고유 아이디")
    private Long productOptionCid;

    @ManyToOne
    @JoinColumn(name = "product_cid", nullable = false)
    @NotNull
    @Schema(description = "상품 cid", example = "cloth")
    private ProductEntity productEntity;

    @NotNull
    @Schema(description = "옵션 사이즈", example = "L")
    @Column(name = "product_Option_size")
    private ProductOptionSize size;

    @NotNull
    @Schema(description = "옵션 재고", example = "12")
    @Column(name = "product_option_stock")
    private int stock;

    public static ProductOptionEntity from(final AddProductOptionReqDto addProductOptionReqDto, final ProductEntity targetProduct) {
        return ProductOptionEntity.builder()
                .productEntity(targetProduct)
                .size(addProductOptionReqDto.getSize())
                .stock(addProductOptionReqDto.getStock())
                .build();
    }

    public ProductOptionEntity updateOptionStock(int newStock) {
        this.stock = newStock;
        return this;
    }
}
