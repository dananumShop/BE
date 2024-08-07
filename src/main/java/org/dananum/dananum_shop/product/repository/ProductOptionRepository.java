package org.dananum.dananum_shop.product.repository;

import org.dananum.dananum_shop.product.web.entity.ProductEntity;
import org.dananum.dananum_shop.product.web.entity.ProductOptionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductOptionRepository extends JpaRepository<ProductOptionEntity, Long> {
    List<ProductOptionEntity> findByProductEntity(ProductEntity targetProduct);

    List<ProductOptionEntity> findAllByProductEntity(ProductEntity targetProduct);
}
