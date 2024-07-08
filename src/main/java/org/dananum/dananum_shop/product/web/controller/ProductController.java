package org.dananum.dananum_shop.product.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dananum.dananum_shop.product.service.ProductService;
import org.dananum.dananum_shop.product.web.dto.list.ProductDetailDto;
import org.dananum.dananum_shop.product.web.dto.list.allProduct.GetAllProductResDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/public/product")
@RequiredArgsConstructor
@Tag(name = "[PUBLIC] 상품 API")
public class ProductController {

    private final ProductService productService;

    @Operation(summary = "전체 상품 조회", description = "전체 상품을 조회하는 api입니다.")
    @GetMapping("/all-product")
    public ResponseEntity<GetAllProductResDto> getAllProduct(
            @PathVariable int page
    ) {
        log.debug("[PRODUCT_PUBLIC] 전체 상품 조회 요청이 들어왔습니다.");
        List<ProductDetailDto> productDetailList = productService.getAllProduct(page);
        log.debug("[PRODUCT_PUBLIC] 전체 상품을 조회했습니다.");

        return ResponseEntity.ok(GetAllProductResDto.successResponse("전체 상품을 조회했습니다.", productDetailList));
    }

//
//    @Operation(summary = "카테고리별 상품 조회", description = "카테고리별 상품을 조회하는 api입니다.")
//
//
//    @Operation(summary = "상품 상세 조회", description = "상품의 상세내용을 조회하는 api입니다.")
}
