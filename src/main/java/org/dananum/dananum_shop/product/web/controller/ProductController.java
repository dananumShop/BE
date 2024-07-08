package org.dananum.dananum_shop.product.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/public/product")
@RequiredArgsConstructor
@Tag(name = "[PUBLIC] 상품 API")
public class ProductController {

//    @Operation(summary = "전체 상품 조회", description = "전체 상품을 조회하는 api입니다.")
//
//
//    @Operation(summary = "카테고리별 상품 조회", description = "카테고리별 상품을 조회하는 api입니다.")
//
//
//    @Operation(summary = "상품 상세 조회", description = "상품의 상세내용을 조회하는 api입니다.")
}
