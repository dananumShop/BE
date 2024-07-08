package org.dananum.dananum_shop.product.service;

import lombok.RequiredArgsConstructor;
import org.dananum.dananum_shop.product.repository.ProductRepository;
import org.dananum.dananum_shop.product.repository.ProductThumbnailRepository;
import org.dananum.dananum_shop.product.util.ProductValidation;
import org.dananum.dananum_shop.product.web.dto.list.ProductDetailDto;
import org.dananum.dananum_shop.product.web.entity.ProductEntity;
import org.dananum.dananum_shop.product.web.entity.ProductThumbnailEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductValidation productValidation;

    private final ProductRepository productRepository;
    private final ProductThumbnailRepository productThumbnailRepository;

    public List<ProductDetailDto> getAllProduct(int page) {

        Pageable pageable = PageRequest.of(page-1, 10);
        Page<ProductEntity> productList = productRepository.findAll(pageable);
        List<ProductDetailDto> productDetailDtoList = new ArrayList<>();

        productValidation.validateProductListIsEmpty(productList);

        for(ProductEntity product: productList) {
            ProductThumbnailEntity productThumbnail = productThumbnailRepository.findByProductEntity(product);

            productDetailDtoList.add(ProductDetailDto.from(product, productThumbnail));
        }

        return productDetailDtoList;
    }
}
