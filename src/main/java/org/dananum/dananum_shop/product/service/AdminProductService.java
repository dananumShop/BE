package org.dananum.dananum_shop.product.service;

import lombok.RequiredArgsConstructor;
import org.dananum.dananum_shop.global.aws.ImageUploadService;
import org.dananum.dananum_shop.product.repository.ProductOptionRepository;
import org.dananum.dananum_shop.product.repository.ProductRepository;
import org.dananum.dananum_shop.product.util.ProductValidation;
import org.dananum.dananum_shop.product.web.dto.crud.AddProductOptionReqDto;
import org.dananum.dananum_shop.product.web.dto.crud.AddProductReqDto;
import org.dananum.dananum_shop.product.web.entity.ProductEntity;
import org.dananum.dananum_shop.product.web.entity.ProductOptionEntity;
import org.dananum.dananum_shop.user.util.UserValidation;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminProductService {

    private final ProductRepository productRepository;
    private final ProductOptionRepository productOptionRepository;

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
    public Long addProduct(User user, AddProductReqDto addProductReq, List<MultipartFile> productInformationImg) {
        userValidation.validateAdminRole(user);

        ProductEntity newProduct = ProductEntity.from(addProductReq);

        productRepository.save(newProduct);

        imageUploadService.uploadProductInformation(productInformationImg, "product_information_img", newProduct);

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
    public void addProductOption(User user, Long productCid, List<AddProductOptionReqDto> addProductOptionReqList) {
        userValidation.validateAdminRole(user);

        ProductEntity targetProduct =  productValidation.validateExistProduct(productCid);

        for(AddProductOptionReqDto option : addProductOptionReqList) {
            productOptionRepository.save(ProductOptionEntity.from(option, targetProduct));
        }
    }
}
