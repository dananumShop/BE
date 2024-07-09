package org.dananum.dananum_shop.product.repository;

import org.dananum.dananum_shop.product.web.entity.ProductEntity;
import org.dananum.dananum_shop.product.web.entity.ProductInformationImgEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductInformationImgRepository extends JpaRepository<ProductInformationImgEntity, Long> {
    List<ProductInformationImgEntity> findByProductEntity(ProductEntity productEntity);

    List<ProductInformationImgEntity> findAllByProductEntity(ProductEntity targetProduct);
}
