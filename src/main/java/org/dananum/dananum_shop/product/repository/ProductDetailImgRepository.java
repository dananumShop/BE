package org.dananum.dananum_shop.product.repository;

import org.dananum.dananum_shop.product.web.entity.ProductDetailImgEntity;
import org.dananum.dananum_shop.product.web.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductDetailImgRepository extends JpaRepository<ProductDetailImgEntity, Long> {

    /**
     * 특정 ProductEntity와 연관된 ProductDetailImgEntity 중에서
     * imageOrder가 가장 작은 엔티티를 반환합니다.
     *
     * @param productEntity 찾으려는 ProductEntity
     * @return imageOrder가 가장 작은 ProductDetailImgEntity
     */
    ProductDetailImgEntity findTopByProductEntityOrderByImageOrderAsc(ProductEntity productEntity);

    List<ProductDetailImgEntity> findAllByProductEntity(ProductEntity targetProduct);
}
