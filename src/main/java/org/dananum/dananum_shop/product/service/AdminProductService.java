package org.dananum.dananum_shop.product.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dananum.dananum_shop.global.aws.ImageUploadService;
import org.dananum.dananum_shop.global.web.enums.ProductCategory;
import org.dananum.dananum_shop.product.repository.ProductDetailImgRepository;
import org.dananum.dananum_shop.product.repository.ProductInformationImgRepository;
import org.dananum.dananum_shop.product.repository.ProductOptionRepository;
import org.dananum.dananum_shop.product.repository.ProductRepository;
import org.dananum.dananum_shop.product.util.ProductValidation;
import org.dananum.dananum_shop.product.web.dto.crud.AddProductOptionReqDto;
import org.dananum.dananum_shop.product.web.dto.crud.AddProductReqDto;
import org.dananum.dananum_shop.product.web.entity.ProductDetailImgEntity;
import org.dananum.dananum_shop.product.web.entity.ProductEntity;
import org.dananum.dananum_shop.product.web.entity.ProductInformationImgEntity;
import org.dananum.dananum_shop.product.web.entity.ProductOptionEntity;
import org.dananum.dananum_shop.user.util.UserValidation;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminProductService {

    private final ProductRepository productRepository;
    private final ProductOptionRepository productOptionRepository;
    private final ProductInformationImgRepository productInformationImgRepository;
    private final ProductDetailImgRepository productDetailImgRepository;

    private final UserValidation userValidation;
    private final ProductValidation productValidation;
    private final ImageUploadService imageUploadService;

    /**
     * 기능 - 상품 생성
     *
     * @param user 관리자 권한을 가진 사용자
     * @param addProductReq 상품 생성 요청 DTO
     * @param productInformationImg 상품 정보 이미지 리스트
     *
     * @return 생성된 상품의 ID
     */
    @Transactional
    public Long addProduct(
            User user, AddProductReqDto addProductReq,
            List<MultipartFile> productDetailImg,
            List<MultipartFile> productInformationImg) {
        userValidation.validateAdminRole(user);

        ProductEntity newProduct = ProductEntity.from(addProductReq);

        productRepository.save(newProduct);

        imageUploadService.uploadProductInformation(productInformationImg, "product_information_img", newProduct);
        imageUploadService.uploadProductDetail(productDetailImg, "product_detail_img", newProduct);

        return newProduct.getProductCid();
    }

    /**
     * 기능 - 상품 옵션 추가
     *
     * @param user 관리자 권한을 가진 사용자
     * @param productCid 대상 상품의 ID
     * @param addProductOptionReqList 상품 옵션 추가 요청 DTO 리스트
     *
     * @return void
     */
    @Transactional
    public void addProductOption(User user, Long productCid, List<AddProductOptionReqDto> addProductOptionReqList) {
        userValidation.validateAdminRole(user);

        ProductEntity targetProduct =  productValidation.validateExistProduct(productCid);

        for(AddProductOptionReqDto option : addProductOptionReqList) {
            productOptionRepository.save(ProductOptionEntity.from(option, targetProduct));
        }
    }

    /**
     * 기능 - 옵션 재고 수정
     *
     * @param user 관리자 권한을 가진 사용자
     * @param optionCid 대상 옵션의 ID
     * @param newStock 새로운 재고 수량
     *
     * @return void
     */
    @Transactional
    public void updateOptionStock(User user, Long optionCid, int newStock) {
        userValidation.validateAdminRole(user);

        ProductOptionEntity targetOption = productValidation.validateExistOption(optionCid);

        targetOption.updateOptionStock(newStock);

        productOptionRepository.save(targetOption);
    }

    /**
     * 상품 정보를 수정하는 메서드
     *
     * @param user 관리자 권한을 가진 사용자
     * @param productCid 수정할 상품의 ID
     * @param addProductReq 수정할 상품의 정보 요청 DTO
     * @param productInformationImg 수정할 상품의 이미지 리스트
     */
    @Transactional
    public void editProduct(User user, Long productCid, AddProductReqDto addProductReq, List<MultipartFile> productDetailImg, List<MultipartFile> productInformationImg) {
        userValidation.validateAdminRole(user);

        ProductEntity targetProduct =  productValidation.validateExistProduct(productCid);

        if(addProductReq != null) {
            editProductInfo(targetProduct, addProductReq);
        }

        if(productDetailImg != null) {
            editProductDetailImg(targetProduct, productInformationImg);
        }

        if(productInformationImg != null) {
            editProductInfoImg(targetProduct, productInformationImg);
        }

        productRepository.save(targetProduct);
    }

    /**
     * 상품의 기본 정보를 수정하는 메서드
     *
     * @param targetProduct 수정할 대상 상품
     * @param addProductReqDto 수정할 상품의 정보 요청 DTO
     */
    private void editProductInfo(ProductEntity targetProduct, AddProductReqDto addProductReqDto) {
        if(addProductReqDto.getProductName() != null && !addProductReqDto.getProductName().isEmpty()) {
            targetProduct.setProductName(addProductReqDto.getProductName());
        }

        if(addProductReqDto.getProductCategory() != null && !addProductReqDto.getProductCategory().isEmpty()) {
            ProductCategory category = ProductCategory.valueOf(addProductReqDto.getProductCategory());
            targetProduct.setProductCategory(category);
        }
    }

    /**
     * 상품의 세부 이미지를 수정하는 메서드
     *
     * @param targetProduct 수정할 대상 상품
     * @param productDetailImg 수정할 상품의 이미지 리스트
     */
    private void editProductDetailImg(ProductEntity targetProduct, List<MultipartFile> productDetailImg) {
        List<ProductDetailImgEntity> oldImageList = productDetailImgRepository.findAllByProductEntity(targetProduct);

        productDetailImgRepository.deleteAll(oldImageList);

        imageUploadService.uploadProductDetail(productDetailImg, "product_detail_img", targetProduct);

        deleteDetailImagesFromS3(oldImageList);
    }

    /**
     * 상품의 상세페이지 이미지를 수정하는 메서드
     *
     * @param targetProduct 수정할 대상 상품
     * @param productInfoImg 수정할 상품의 이미지 리스트
     */
    private void editProductInfoImg(ProductEntity targetProduct, List<MultipartFile> productInfoImg) {
        List<ProductInformationImgEntity> oldImageList = productInformationImgRepository.findByProductEntity(targetProduct);

        productInformationImgRepository.deleteAll(oldImageList);

        imageUploadService.uploadProductInformation(productInfoImg, "product_information_img", targetProduct);

        deleteInfoImagesFromS3(oldImageList);
    }

    /**
     * 상품을 삭제하는 메서드
     *
     * @param user 관리자 권한을 가진 사용자
     * @param productCid 삭제할 상품의 ID
     */
    @Transactional
    public void deleteProduct(User user, Long productCid) {
        userValidation.validateAdminRole(user);

        ProductEntity targetProduct =  productValidation.validateExistProduct(productCid);

        deleteProductOption(targetProduct);
        List<ProductDetailImgEntity> detailImageList = deleteProductDetailImage(targetProduct);
        List<ProductInformationImgEntity> imageList = deleteProductInfoImage(targetProduct);

        productRepository.delete(targetProduct);

        deleteDetailImagesFromS3(detailImageList);
        deleteInfoImagesFromS3(imageList);
    }

    /**
     * 상품에 연결된 옵션을 삭제하는 메서드
     *
     * @param targetProduct 삭제할 대상 상품
     */
    private void deleteProductOption(ProductEntity targetProduct) {
        List<ProductOptionEntity> optionList = productOptionRepository.findByProductEntity(targetProduct);

        productOptionRepository.deleteAll(optionList);
    }

    /**
     * 상품에 연결된 이미지를 삭제하고 이미지 리스트를 반환하는 메서드
     *
     * @param targetProduct 삭제할 대상 상품
     * @return 삭제된 상품 이미지 리스트
     */
    private List<ProductDetailImgEntity> deleteProductDetailImage(ProductEntity targetProduct) {
        List<ProductDetailImgEntity> imageList = productDetailImgRepository.findAllByProductEntity(targetProduct);

        productDetailImgRepository.deleteAll(imageList);

        return imageList;
    }

    /**
     * 상품에 연결된 이미지를 삭제하고 이미지 리스트를 반환하는 메서드
     *
     * @param targetProduct 삭제할 대상 상품
     * @return 삭제된 상품 이미지 리스트
     */
    private List<ProductInformationImgEntity> deleteProductInfoImage(ProductEntity targetProduct) {
        List<ProductInformationImgEntity> imageList = productInformationImgRepository.findByProductEntity(targetProduct);

        productInformationImgRepository.deleteAll(imageList);

        return imageList;
    }

    /**
     * S3에서 이미지를 비동기적으로 삭제하는 메서드
     *
     * @param images 삭제할 이미지 엔티티 리스트
     */
    @Async
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void deleteDetailImagesFromS3(List<ProductDetailImgEntity> images) {
        for (ProductDetailImgEntity image : images) {
            imageUploadService.deleteImage(image.getImagePath());
        }
    }

    /**
     * S3에서 이미지를 비동기적으로 삭제하는 메서드
     *
     * @param images 삭제할 이미지 엔티티 리스트
     */
    @Async
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void deleteInfoImagesFromS3(List<ProductInformationImgEntity> images) {
        for (ProductInformationImgEntity image : images) {
            imageUploadService.deleteImage(image.getImagePath());
        }
    }
}
