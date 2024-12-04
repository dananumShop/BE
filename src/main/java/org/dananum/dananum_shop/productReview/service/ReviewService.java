package org.dananum.dananum_shop.productReview.service;

import lombok.RequiredArgsConstructor;
import org.dananum.dananum_shop.product.web.dto.review.AddReviewReqDto;
import org.dananum.dananum_shop.user.util.UserValidation;
import org.dananum.dananum_shop.user.web.entity.user.UserEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final UserValidation userValidation;

    public void addReview(User user, AddReviewReqDto addReviewReqDto) {
        UserEntity userEntity = userValidation.validateExistUser(user.getUsername());

        
    }
}
