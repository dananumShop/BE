package org.dananum.dananum_shop.productLike.web.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dananum.dananum_shop.productLike.service.ProductLikeService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/public/product/like")
@RequiredArgsConstructor
@Tag(name = "[PUBLIC] 상품 API")
public class ProductLikeController {

    private final ProductLikeService productLikeService;
}
