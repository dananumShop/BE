package org.dananum.dananum_shop.product.service;

import lombok.RequiredArgsConstructor;
import org.dananum.dananum_shop.global.web.enums.ProductCategory;
import org.dananum.dananum_shop.global.web.enums.ProductGender;
import org.dananum.dananum_shop.product.repository.ProductDetailImgRepository;
import org.dananum.dananum_shop.product.repository.ProductRepository;
import org.dananum.dananum_shop.product.util.ProductValidation;
import org.dananum.dananum_shop.product.web.dto.list.ProductDetailDto;
import org.dananum.dananum_shop.product.web.entity.ProductDetailImgEntity;
import org.dananum.dananum_shop.product.web.entity.ProductEntity;
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
    private final ProductDetailImgRepository productDetailImgRepository;

    /**
     * 모든 상품을 페이지 단위로 가져옵니다.
     *
     * @param page 가져올 페이지 번호
     * @return ProductDetailDto 리스트
     */
    public List<ProductDetailDto> getAllProduct(int page) {

        Pageable pageable = PageRequest.of(page-1, 10);
        Page<ProductEntity> productList = productRepository.findAll(pageable);

        return EntityToDto(productList);
    }

    /**
     * 특정 카테고리의 상품을 페이지 단위로 가져옵니다.
     *
     * @param category 가져올 상품의 카테고리
     * @param page 가져올 페이지 번호
     * @return ProductDetailDto 리스트
     */
    public List<ProductDetailDto> getProductByCategory(ProductCategory category, int page) {
        Pageable pageable = PageRequest.of(page-1, 10);
        Page<ProductEntity> productList = productRepository.findAllByProductCategory(category, pageable);

        return EntityToDto(productList);
    }

    /**
     * 특정 성별 상품을 페이지 단위로 가져옵니다.
     *
     * @param gender 가져올 상품의 카테고리
     * @param page 가져올 페이지 번호
     * @return ProductDetailDto 리스트
     */
    public List<ProductDetailDto> getProductByGender(ProductGender gender, int page) {
        Pageable pageable = PageRequest.of(page-1, 10);
        Page<ProductEntity> productList = productRepository.findAllByProductGender(gender, pageable);

        return EntityToDto(productList);
    }

    /**
     * ProductEntity 페이지를 ProductDetailDto 리스트로 변환합니다.
     *
     * @param productList 변환할 ProductEntity 페이지
     * @return ProductDetailDto 리스트
     */
    private List<ProductDetailDto> EntityToDto(Page<ProductEntity> productList) {

        List<ProductDetailDto> productDetailDtoList = new ArrayList<>();

        productValidation.validateProductListIsEmpty(productList);

        for(ProductEntity product: productList) {
            ProductDetailImgEntity productDetailImg = productDetailImgRepository.findTopByProductEntityOrderByImageOrderAsc(product);

            productDetailDtoList.add(ProductDetailDto.from(product, productDetailImg));
        }

        return productDetailDtoList;
    }

}
