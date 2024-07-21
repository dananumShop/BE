package org.dananum.dananum_shop.productLike.web.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import org.dananum.dananum_shop.global.web.entity.TimeEntity;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "product_like_table")
public class ProductLikeEntity extends TimeEntity {
    @Id
    @Column(name = "product_like_cid")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "상품 찜 고유 아이디")
    private Long productLikeCid;

    @Schema(description = "유저 cid")
    @Column(name = "user_cid")
    private Long userCid;

    @Schema(description = "상품 cid")
    @Column(name = "product_cid")
    private Long productCid;

    public static ProductLikeEntity from(Long userCid, Long productCid) {
        return ProductLikeEntity.builder()
                .userCid(userCid)
                .productCid(productCid)
                .build();
    }
}
