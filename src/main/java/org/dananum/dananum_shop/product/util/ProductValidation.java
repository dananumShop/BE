package org.dananum.dananum_shop.product.util;

import lombok.RequiredArgsConstructor;
import org.dananum.dananum_shop.global.web.advice.exception.CustomNoSuchElementException;
import org.dananum.dananum_shop.global.web.advice.exception.CustomNotFoundException;
import org.dananum.dananum_shop.product.repository.ProductOptionRepository;
import org.dananum.dananum_shop.product.repository.ProductRepository;
import org.dananum.dananum_shop.product.web.entity.ProductEntity;
import org.dananum.dananum_shop.product.web.entity.ProductOptionEntity;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductValidation {

    private final ProductRepository productRepository;
    private final ProductOptionRepository productOptionRepository;

    // 상품의 유무 조회
    public ProductEntity validateExistProduct(Long productCid) {
        return productRepository.findById(productCid)
                .orElseThrow(() -> new CustomNotFoundException("일치하는 상품이 없습니다."));
    }

    // 옵션의 유무 조회
    public ProductOptionEntity validateExistOption(Long optionCid) {
        return productOptionRepository.findById(optionCid)
                .orElseThrow(() -> new CustomNotFoundException("일치하는 옵션이 없습니다."));
    }

    // 상품 비어있는지 검증하는 메서드
    public void validateProductListIsEmpty(Page<ProductEntity> postList) {
        if(postList.isEmpty()){
            throw new CustomNoSuchElementException("일치하는 상품이 없습니다.");
        }
    }
}
