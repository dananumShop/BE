package org.dananum.dananum_shop.product.util;

import lombok.RequiredArgsConstructor;
import org.dananum.dananum_shop.global.web.advice.exception.CustomNotFoundException;
import org.dananum.dananum_shop.product.repository.ProductRepository;
import org.dananum.dananum_shop.product.web.entity.ProductEntity;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductValidation {

    private final ProductRepository productRepository;

    public ProductEntity validateExistProduct(Long productCid) {
        return productRepository.findById(productCid)
                .orElseThrow(() -> new CustomNotFoundException("일치하는 상품이 없습니다."));
    }

}
