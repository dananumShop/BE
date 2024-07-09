package org.dananum.dananum_shop.product.service;

import lombok.RequiredArgsConstructor;
import org.dananum.dananum_shop.global.web.enums.ProductCategory;
import org.dananum.dananum_shop.global.web.enums.ProductGender;
import org.dananum.dananum_shop.product.repository.ProductDetailImgRepository;
import org.dananum.dananum_shop.product.repository.ProductInformationImgRepository;
import org.dananum.dananum_shop.product.repository.ProductOptionRepository;
import org.dananum.dananum_shop.product.repository.ProductRepository;
import org.dananum.dananum_shop.product.util.ProductValidation;
import org.dananum.dananum_shop.product.web.dto.list.ProductDetailDto;
import org.dananum.dananum_shop.product.web.dto.list.detail.ProductDetailPageDto;
import org.dananum.dananum_shop.product.web.dto.list.detail.options.GetDetailImgDto;
import org.dananum.dananum_shop.product.web.dto.list.detail.options.GetInfoImgDto;
import org.dananum.dananum_shop.product.web.dto.list.detail.options.GetOptionDto;
import org.dananum.dananum_shop.product.web.entity.ProductDetailImgEntity;
import org.dananum.dananum_shop.product.web.entity.ProductEntity;
import org.dananum.dananum_shop.product.web.entity.ProductInformationImgEntity;
import org.dananum.dananum_shop.product.web.entity.ProductOptionEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductValidation productValidation;

    private final ProductRepository productRepository;
    private final ProductDetailImgRepository productDetailImgRepository;
    private final ProductOptionRepository productOptionRepository;
    private final ProductInformationImgRepository productInformationImgRepository;

    /**
     * 모든 상품을 페이지 단위로 가져옵니다.
     *
     * @param page 가져올 페이지 번호
     * @return ProductDetailPageDto 리스트
     */
    @Transactional(readOnly = true)
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
     * @return ProductDetailPageDto 리스트
     */
    @Transactional(readOnly = true)
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
     * @return ProductDetailPageDto 리스트
     */
    @Transactional(readOnly = true)
    public List<ProductDetailDto> getProductByGender(ProductGender gender, int page) {
        Pageable pageable = PageRequest.of(page-1, 10);
        Page<ProductEntity> productList = productRepository.findAllByProductGender(gender, pageable);

        return EntityToDto(productList);
    }

    /**
     * ProductEntity 페이지를 ProductDetailPageDto 리스트로 변환합니다.
     *
     * @param productList 변환할 ProductEntity 페이지
     * @return ProductDetailPageDto 리스트
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

    /**
     * 상품 상세 정보를 조회하는 메서드
     *
     * @param productCid 조회할 상품의 고유 아이디
     * @return 조회된 상품의 상세 정보
     */
    @Transactional(readOnly = true)
    public ProductDetailPageDto getProductDetail(Long productCid) {
        ProductEntity targetProduct = productValidation.validateExistProduct(productCid);

        List<GetOptionDto> optionDtoList = getProductOptionList(targetProduct);
        List<GetDetailImgDto> detailImageDtoList = getProductDetailImgList(targetProduct);
        List<GetInfoImgDto> infoImgDtoList = getProductInfoImgList(targetProduct);

        return ProductDetailPageDto.from(targetProduct, optionDtoList, detailImageDtoList, infoImgDtoList);
    }

    /**
     * 상품 옵션 리스트를 조회하는 메서드
     *
     * @param targetProduct 조회할 상품 엔티티
     * @return 상품 옵션 DTO 리스트
     */
    private List<GetOptionDto> getProductOptionList(ProductEntity targetProduct) {
        List<ProductOptionEntity> optionList = productOptionRepository.findAllByProductEntity(targetProduct);

        return optionEntityToDto(optionList);
    }

    /**
     * 상품 옵션 엔티티를 DTO로 변환하는 메서드
     *
     * @param productOptionEntities 변환할 상품 옵션 엔티티 리스트
     * @return 변환된 상품 옵션 DTO 리스트
     */
    private List<GetOptionDto> optionEntityToDto(List<ProductOptionEntity> productOptionEntities) {
        List<GetOptionDto> optionDtoList = new ArrayList<>();

        for(ProductOptionEntity option: productOptionEntities) {
            optionDtoList.add(GetOptionDto.from(option));
        }

        return optionDtoList;
    }

    /**
     * 상품 상세 이미지 리스트를 조회하는 메서드
     *
     * @param targetProduct 조회할 상품 엔티티
     * @return 상품 상세 이미지 DTO 리스트
     */
    private List<GetDetailImgDto> getProductDetailImgList(ProductEntity targetProduct) {
        List<ProductDetailImgEntity> detailImgList = productDetailImgRepository.findAllByProductEntity(targetProduct);

        return detailImgEntityToDto(detailImgList);
    }

    /**
     * 상품 상세 이미지 엔티티를 DTO로 변환하는 메서드
     *
     * @param detailImgList 변환할 상품 상세 이미지 엔티티 리스트
     * @return 변환된 상품 상세 이미지 DTO 리스트
     */
    private List<GetDetailImgDto> detailImgEntityToDto(List<ProductDetailImgEntity> detailImgList) {
        List<GetDetailImgDto> detailImgDtoList = new ArrayList<>();

        for(ProductDetailImgEntity detailImg: detailImgList) {
            detailImgDtoList.add(GetDetailImgDto.from(detailImg));
        }

        return detailImgDtoList;
    }

    /**
     * 상품 정보 이미지 리스트를 조회하는 메서드
     *
     * @param targetProduct 조회할 상품 엔티티
     * @return 상품 정보 이미지 DTO 리스트
     */
    private List<GetInfoImgDto> getProductInfoImgList(ProductEntity targetProduct) {
        List<ProductInformationImgEntity> infoImgList = productInformationImgRepository.findAllByProductEntity(targetProduct);

        return infoImgEntityToDto(infoImgList);
    }

    /**
     * 상품 정보 이미지 엔티티를 DTO로 변환하는 메서드
     *
     * @param infoImgList 변환할 상품 정보 이미지 엔티티 리스트
     * @return 변환된 상품 정보 이미지 DTO 리스트
     */
    private List<GetInfoImgDto> infoImgEntityToDto(List<ProductInformationImgEntity> infoImgList) {
        List<GetInfoImgDto> infoImgDtoList = new ArrayList<>();

        for(ProductInformationImgEntity infoImg: infoImgList) {
            infoImgDtoList.add(GetInfoImgDto.from(infoImg));
        }

        return infoImgDtoList;
    }

}
