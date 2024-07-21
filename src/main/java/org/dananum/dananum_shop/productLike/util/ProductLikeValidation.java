package org.dananum.dananum_shop.productLike.util;

import lombok.RequiredArgsConstructor;
import org.dananum.dananum_shop.global.web.advice.exception.CustomNoSuchElementException;
import org.dananum.dananum_shop.global.web.advice.exception.CustomNotFoundException;
import org.dananum.dananum_shop.productLike.repository.ProductLikeRepository;
import org.dananum.dananum_shop.productLike.web.entity.ProductLikeEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductLikeValidation {

    private final ProductLikeRepository productLikeRepository;

    public Page<ProductLikeEntity> validateExistLikeList(Long userCid, int page) {

        Pageable pageable = PageRequest.of(page-1, 10);
        Page<ProductLikeEntity> productLikeList = productLikeRepository.findAllByUserCid(userCid, pageable);

        if(productLikeList.isEmpty()) {
            throw new CustomNoSuchElementException("찜 목록이 비어있습니다.");
        }

        return productLikeList;
    }

    public ProductLikeEntity validateExistLikeEntity(Long productLikeCid) {
        ProductLikeEntity likeEntity = productLikeRepository.findById(productLikeCid)
                .orElseThrow(() -> new CustomNotFoundException("일치하는 찜 상품이 없습니다."));

        return likeEntity;
    }
}
