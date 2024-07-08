package org.dananum.dananum_shop.product.repository;

import org.dananum.dananum_shop.product.web.entity.ProductEntity;
import org.dananum.dananum_shop.product.web.entity.ProductThumbnailEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductThumbnailRepository extends JpaRepository<ProductThumbnailEntity, Long> {

    ProductThumbnailEntity findByProductEntity(ProductEntity targetProduct);
}
