package org.dananum.dananum_shop.productLike.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dananum.dananum_shop.global.web.dto.CommonResponseDto;
import org.dananum.dananum_shop.product.web.dto.list.ProductDetailDto;
import org.dananum.dananum_shop.product.web.dto.list.allProduct.GetAllProductResDto;
import org.dananum.dananum_shop.productLike.service.ProductLikeService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@Slf4j
@RequestMapping("/public/product/like")
@RequiredArgsConstructor
@Tag(name = "[PUBLIC] 상품 API")
public class ProductLikeController {

    private final ProductLikeService productLikeService;

    @Operation(summary = "찜 상품 추가", description = "찜 목록에 상품을 추가하는 api입니다.")
    @PostMapping("/")
    public ResponseEntity<CommonResponseDto> addLikeProduct(
            @AuthenticationPrincipal User user,
            @RequestParam Long productCid
    ) {
        log.debug("[PRODUCT_PUBLIC] 찜 상품을 추가하는 요청이 들어왔습니다.");
        productLikeService.addLikeService(user, productCid);
        log.debug("[PRODUCT_PUBLIC] 찜 상품을 추가했습니다.");

        return ResponseEntity.ok(CommonResponseDto.successResponse("성공적으로 찜을 추가했습니다."));
    }

    @Operation(summary = "찜 목록 조회", description = "찜 목록을 조회하는 api입니다.")
    @GetMapping("/{page}")
    public ResponseEntity<GetAllProductResDto> getLikeList(
            @AuthenticationPrincipal User user,
            @PathVariable int page
    ) {
        log.debug("[PRODUCT_PUBLIC] 찜 목록 조회 요청이 들어왔습니다.");
        List<ProductDetailDto> likeList = productLikeService.getLikeList(user, page);
        log.debug("[PRODUCT_PUBLIC] 찜 목록을 불러왔습니다.");

        return ResponseEntity.ok(GetAllProductResDto.successResponse("성공적으로 찜 목록을 불러왔습니다.", likeList));
    }
}
