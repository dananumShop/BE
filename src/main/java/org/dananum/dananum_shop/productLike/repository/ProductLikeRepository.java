package org.dananum.dananum_shop.productLike.repository;

import org.dananum.dananum_shop.productLike.web.entity.ProductLikeEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductLikeRepository extends JpaRepository<ProductLikeEntity, Long> {
    Page<ProductLikeEntity> findAllByUserCid(Long userCid, Pageable pageable);
}
