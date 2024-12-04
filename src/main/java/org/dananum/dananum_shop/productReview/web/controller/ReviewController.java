package org.dananum.dananum_shop.productReview.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dananum.dananum_shop.global.web.dto.CommonResponseDto;
import org.dananum.dananum_shop.productReview.service.ReviewService;
import org.dananum.dananum_shop.product.web.dto.review.AddReviewReqDto;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/public/product/review")
@RequiredArgsConstructor
@Tag(name = "[PUBLIC] 상품 API")
public class ReviewController {

    private final ReviewService reviewService;

    @Operation(summary = "리뷰 작성", description = "리뷰 작성에 사용되는 api입니다.")
    @PostMapping("/")
    public ResponseEntity<CommonResponseDto> addReview(
            @AuthenticationPrincipal User user,
            @RequestBody AddReviewReqDto addReviewReqDto
            ) {
        log.debug("[PRODUCT_PUBLIC] 리뷰 작성 요청이 들어왔습니다.");
        reviewService.addReview(user, addReviewReqDto);
        log.debug("[PRODUCT_PUBLIC] 리뷰를 추가했습니다.");

        return ResponseEntity.ok(CommonResponseDto.createSuccessResponse("리뷰를 성공적으로 추가했습니다."));
    }
}
