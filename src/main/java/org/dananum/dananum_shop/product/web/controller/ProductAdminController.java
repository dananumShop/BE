package org.dananum.dananum_shop.product.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dananum.dananum_shop.global.web.dto.CommonResponseDto;
import org.dananum.dananum_shop.product.service.AdminProductService;
import org.dananum.dananum_shop.product.web.dto.crud.AddProductOptionReqDto;
import org.dananum.dananum_shop.product.web.dto.crud.AddProductReqDto;
import org.dananum.dananum_shop.product.web.dto.crud.AddProductResDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/admin/product")
@RequiredArgsConstructor
@Tag(name = "[ADMIN] 상품 API")
public class ProductAdminController {

    private final AdminProductService adminProductService;

    @Operation(summary = "상품 생성", description = "상품 생성을 다루는 api입니다.")
    @PostMapping(value = "/", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AddProductResDto> createProduct(
            @AuthenticationPrincipal User user,
            @RequestPart(name = "productInformation") @Parameter(schema = @Schema(type = "string", format = "binary")) AddProductReqDto addProductReq,
            @RequestPart(name = "productInformationImg") List<MultipartFile> productInformationImg
            ) {
        log.debug("[PRODUCT_ADMIN] 물품 추가 요청이 들어왔습니다.");
        Long productCid = adminProductService.addProduct(user, addProductReq, productInformationImg);
        log.debug("[PRODUCT_ADMIN] 물품 추가 작업을 성공적으로 완료하였습니다.");

        return ResponseEntity.status(HttpStatus.CREATED).body(AddProductResDto.createSuccessResponse("물품 추가 완료", productCid));
    }

    @Operation(summary = "상품 옵션 생성", description = "상품 옵션 생성을 다루는 api입니다.")
    @PostMapping("/option")
    public ResponseEntity<CommonResponseDto> createProductOption(
            @AuthenticationPrincipal User user,
            @RequestParam Long productCid,
            @RequestBody List<AddProductOptionReqDto> addProductOptionReqList
            ) {
        log.debug("[PRODUCT_ADMIN] 물품 옵션 추가 요청이 들어왔습니다.");
        adminProductService.addProductOption(user,productCid, addProductOptionReqList);
        log.debug("[PRODUCT_ADMIN] 물품 옵션 추가 작업을 성공적으로 완료하였습니다.");

        return ResponseEntity.status(HttpStatus.CREATED).body(CommonResponseDto.createSuccessResponse("물품 옵션 추가 완료"));
    }

//
//    @Operation(summary = "상품 수정", description = "상품 수정을 다루는 api입니다.")
//
//
//    @Operation(summary = "상품 삭제", description = "상품 삭제를 다루는 api입니다.")
}
