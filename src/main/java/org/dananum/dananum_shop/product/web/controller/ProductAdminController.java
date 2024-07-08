package org.dananum.dananum_shop.product.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/admin/product")
@RequiredArgsConstructor
@Tag(name = "[ADMIN] 상품 API")
public class ProductAdminController {

    @Operation(summary = "상품 생성", description = "상품 생성을 다루는 api입니다.")


    @Operation(summary = "상품 수정", description = "상품 수정을 다루는 api입니다.")


    @Operation(summary = "상품 삭제", description = "상품 삭제를 다루는 api입니다.")
}
