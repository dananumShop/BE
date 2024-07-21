package org.dananum.dananum_shop.productLike.service;

import lombok.RequiredArgsConstructor;
import org.dananum.dananum_shop.product.util.ProductValidation;
import org.dananum.dananum_shop.productLike.repository.ProductLikeRepository;
import org.dananum.dananum_shop.productLike.web.entity.ProductLikeEntity;
import org.dananum.dananum_shop.user.util.UserValidation;
import org.dananum.dananum_shop.user.web.entity.user.UserEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductLikeService {

    private final ProductLikeRepository productLikeRepository;

    private final ProductValidation productValidation;
    private final UserValidation userValidation;

    @Transactional
    public void addLikeService(User user, Long productCid) {
        UserEntity userEntity = userValidation.validateExistUser(user.getUsername());

        productValidation.validateExistProduct(productCid);

        productLikeRepository.save(ProductLikeEntity.from(userEntity.getUserCid(), productCid));
    }


}
