package org.dananum.dananum_shop.inquiry.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dananum.dananum_shop.global.web.dto.CommonResponseDto;
import org.dananum.dananum_shop.inquiry.service.InquiryService;
import org.dananum.dananum_shop.inquiry.web.dto.add.AddInquiryReqDto;
import org.dananum.dananum_shop.product.web.dto.list.detail.GetProductDetailResDto;
import org.dananum.dananum_shop.product.web.dto.list.detail.ProductDetailPageDto;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/public/inquiry")
@RequiredArgsConstructor
@Tag(name = "[PUBLIC] 문의 API")
public class InquiryController {

    private final InquiryService inquiryService;

    @Operation(summary = "문의 작성", description = "문의를 추가하는 api 입니다.")
    @PostMapping("/")
    public ResponseEntity<CommonResponseDto> addInquiry(
            @AuthenticationPrincipal User user,
            @RequestBody AddInquiryReqDto addInquiryReqDto
            ) {
        log.debug("[INQUIRY_PUBLIC] 문의 생성 요청이 들어왔습니다.");
        inquiryService.addInquiry(user, addInquiryReqDto);
        log.debug("[INQUIRY_PUBLIC] 성공적으로 문의를 추가하였습니다.");

        return ResponseEntity.ok(CommonResponseDto.createSuccessResponse("문의를 추가했습니다."));
    }

}
