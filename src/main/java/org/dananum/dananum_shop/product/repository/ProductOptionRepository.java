package org.dananum.dananum_shop.product.repository;

import org.dananum.dananum_shop.product.web.entity.ProductOptionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductOptionRepository extends JpaRepository<ProductOptionEntity, Long> {
}
