package org.dananum.dananum_shop.productLike.service;

import lombok.RequiredArgsConstructor;
import org.dananum.dananum_shop.product.repository.ProductDetailImgRepository;
import org.dananum.dananum_shop.product.util.ProductValidation;
import org.dananum.dananum_shop.product.web.dto.list.ProductDetailDto;
import org.dananum.dananum_shop.product.web.entity.ProductDetailImgEntity;
import org.dananum.dananum_shop.product.web.entity.ProductEntity;
import org.dananum.dananum_shop.productLike.repository.ProductLikeRepository;
import org.dananum.dananum_shop.productLike.util.ProductLikeValidation;
import org.dananum.dananum_shop.productLike.web.entity.ProductLikeEntity;
import org.dananum.dananum_shop.user.util.UserValidation;
import org.dananum.dananum_shop.user.web.entity.user.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductLikeService {

    private final ProductLikeRepository productLikeRepository;
    private final ProductDetailImgRepository productDetailImgRepository;

    private final ProductLikeValidation productLikeValidation;
    private final ProductValidation productValidation;
    private final UserValidation userValidation;

    @Transactional
    public void addLikeService(User user, Long productCid) {
        UserEntity userEntity = userValidation.validateExistUser(user.getUsername());

        productValidation.validateExistProduct(productCid);

        productLikeRepository.save(ProductLikeEntity.from(userEntity.getUserCid(), productCid));
    }


    public List<ProductDetailDto> getLikeList(User user, int page) {
        UserEntity userEntity = userValidation.validateExistUser(user.getUsername());

        Page<ProductLikeEntity> likeList = productLikeValidation.validateExistLikeList(userEntity.getUserCid(), page);

        return getLikeDtoList(likeList);
    }

    private List<ProductDetailDto> getLikeDtoList(Page<ProductLikeEntity> likeList) {

        List<ProductDetailDto> productDtoList = new ArrayList<>();

        for(ProductLikeEntity productLike : likeList) {

            ProductEntity productEntity = productValidation.validateExistProduct(productLike.getProductCid());

            ProductDetailImgEntity productDetailImg = productDetailImgRepository.findTopByProductEntityOrderByImageOrderAsc(productEntity);

            productDtoList.add(ProductDetailDto.from(productEntity, productDetailImg));
        }

        return productDtoList;
    }

    public void deleteLikeProduct(User user, Long productLikeCid) {
        userValidation.validateExistUser(user.getUsername());

        ProductLikeEntity likeEntity = productLikeValidation.validateExistLikeEntity(productLikeCid);

        productLikeRepository.delete(likeEntity);
    }
}
