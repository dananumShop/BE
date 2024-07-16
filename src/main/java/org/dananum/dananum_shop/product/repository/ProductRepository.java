package org.dananum.dananum_shop.product.repository;

import org.dananum.dananum_shop.global.web.enums.product.ProductCategory;
import org.dananum.dananum_shop.global.web.enums.product.ProductGender;
import org.dananum.dananum_shop.product.web.entity.ProductEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long> {

    Page<ProductEntity> findAll(Pageable pageable);

    Page<ProductEntity> findAllByProductCategory(ProductCategory productCategory, Pageable pageable);

    Page<ProductEntity> findAllByProductGender(ProductGender gender, Pageable pageable);
}
