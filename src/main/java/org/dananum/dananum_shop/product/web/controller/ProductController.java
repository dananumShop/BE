package org.dananum.dananum_shop.product.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dananum.dananum_shop.global.web.enums.product.ProductCategory;
import org.dananum.dananum_shop.global.web.enums.product.ProductGender;
import org.dananum.dananum_shop.product.service.ProductService;
import org.dananum.dananum_shop.product.web.dto.list.allProduct.GetAllProductResDto;
import org.dananum.dananum_shop.product.web.dto.list.detail.GetProductDetailResDto;
import org.dananum.dananum_shop.product.web.dto.list.detail.ProductDetailPageDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/public/product")
@RequiredArgsConstructor
@Tag(name = "[PUBLIC] 상품 API")
public class ProductController {

    private final ProductService productService;

    @Operation(summary = "전체 상품 조회", description = "전체 상품을 조회하는 api입니다.")
    @GetMapping("/all-product/{page}")
    public ResponseEntity<GetAllProductResDto> getAllProduct(
            @PathVariable int page
    ) {
        log.debug("[PRODUCT_PUBLIC] 전체 상품 조회 요청이 들어왔습니다.");
        List<org.dananum.dananum_shop.product.web.dto.list.ProductDetailDto> productDetailList = productService.getAllProduct(page);
        log.debug("[PRODUCT_PUBLIC] 전체 상품을 조회했습니다.");

        return ResponseEntity.ok(GetAllProductResDto.successResponse("전체 상품을 조회했습니다.", productDetailList));
    }


    @Operation(summary = "카테고리별 상품 조회", description = "카테고리별 상품을 조회하는 api입니다.")
    @GetMapping("/category-product/{page}")
    public ResponseEntity<GetAllProductResDto> getProductByCategory(
            @PathVariable int page,
            @RequestParam ProductCategory category
    ) {
        log.debug("[PRODUCT_PUBLIC] 카테고리 상품 조회 요청이 들어왔습니다.");
        List<org.dananum.dananum_shop.product.web.dto.list.ProductDetailDto> productDetailList = productService.getProductByCategory(category, page);
        log.debug("[PRODUCT_PUBLIC] 카테고리 상품을 조회했습니다.");

        return ResponseEntity.ok(GetAllProductResDto.successResponse("카테고리별 상품을 조회했습니다.", productDetailList));
    }

    @Operation(summary = "성별 상품 조회", description = "성별에 따른 상품을 조회하는 api입니다.")
    @GetMapping("/gender-product/{page}")
    public ResponseEntity<GetAllProductResDto> getProductByGender(
            @PathVariable int page,
            @RequestParam ProductGender gender
    ) {
        log.debug("[PRODUCT_PUBLIC] 카테고리 상품 조회 요청이 들어왔습니다.");
        List<org.dananum.dananum_shop.product.web.dto.list.ProductDetailDto> productDetailList = productService.getProductByGender(gender, page);
        log.debug("[PRODUCT_PUBLIC] 카테고리 상품을 조회했습니다.");

        return ResponseEntity.ok(GetAllProductResDto.successResponse("성별에 따른 상품을 조회했습니다.", productDetailList));
    }

    @Operation(summary = "상품 상세 조회", description = "상품의 상세내용을 조회하는 api입니다.")
    @GetMapping("/detail/{productCid}")
    public ResponseEntity<GetProductDetailResDto> getProductDetail(
            @PathVariable Long productCid
    ) {
        log.debug("[PRODUCT_PUBLIC] 카테고리 상품 조회 요청이 들어왔습니다.");
        ProductDetailPageDto productDetail = productService.getProductDetail(productCid);
        log.debug("[PRODUCT_PUBLIC] 카테고리 상품을 조회했습니다.");

        return ResponseEntity.ok(GetProductDetailResDto.successResponse("성별에 따른 상품을 조회했습니다.", productDetail));
    }
}
