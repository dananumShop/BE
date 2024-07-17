package org.dananum.dananum_shop.inquiry.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dananum.dananum_shop.global.web.dto.CommonResponseDto;
import org.dananum.dananum_shop.global.web.enums.inquiry.InquiryStatus;
import org.dananum.dananum_shop.inquiry.service.AdminInquiryService;
import org.dananum.dananum_shop.inquiry.web.dto.get.GetInquiryDto;
import org.dananum.dananum_shop.inquiry.web.dto.get.GetInquiryListResDto;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/admin/inquiry")
@RequiredArgsConstructor
@Tag(name = "[ADMIN] 문의 API")
public class AdminInquiryController {

    private final AdminInquiryService adminInquiryService;

    @Operation(summary = "전체 문의 조회", description = "작성된 전체 문의를 조회하는 api 입니다.")
    @GetMapping("/all")
    public ResponseEntity<GetInquiryListResDto> getAllInquiry(
            @AuthenticationPrincipal User user,
            @RequestParam int page
    ) {
        log.debug("[INQUIRY_ADMIN] 문의 전체 조회 요청이 들어왔습니다.");
        List<GetInquiryDto> inquiryDtoList = adminInquiryService.getAllInquiry(user, page);
        log.debug("[INQUIRY_ADMIN] 성공적으로 전체 문의를 조회했습니다.");

        return ResponseEntity.ok(GetInquiryListResDto.successResponse("문의를 조회 했습니다.", inquiryDtoList));
    }

    @Operation(summary = "상태별 문의 조회", description = "상태별 문의를 조회하는 api 입니다.")
    @GetMapping("/inquiry-status")
    public ResponseEntity<GetInquiryListResDto> getAllInquiry(
            @AuthenticationPrincipal User user,
            @RequestParam int page,
            @RequestParam InquiryStatus inquiryStatus
            ) {
        log.debug("[INQUIRY_ADMIN] 문의 전체 조회 요청이 들어왔습니다.");
        List<GetInquiryDto> inquiryDtoList = adminInquiryService.getInquiryByStatus(user, page, inquiryStatus);
        log.debug("[INQUIRY_ADMIN] 성공적으로 전체 문의를 조회했습니다.");

        return ResponseEntity.ok(GetInquiryListResDto.successResponse("문의를 조회 했습니다.", inquiryDtoList));
    }
}
