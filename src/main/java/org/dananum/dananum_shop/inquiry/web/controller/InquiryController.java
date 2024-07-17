package org.dananum.dananum_shop.inquiry.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dananum.dananum_shop.global.web.dto.CommonResponseDto;
import org.dananum.dananum_shop.inquiry.service.InquiryService;
import org.dananum.dananum_shop.inquiry.web.dto.add.AddInquiryCommentReqDto;
import org.dananum.dananum_shop.inquiry.web.dto.add.AddInquiryReqDto;
import org.dananum.dananum_shop.inquiry.web.dto.get.GetInquiryDto;
import org.dananum.dananum_shop.inquiry.web.dto.get.GetInquiryListResDto;
import org.dananum.dananum_shop.inquiry.web.dto.get.detail.GetInquiryDetailResDto;
import org.dananum.dananum_shop.inquiry.web.dto.get.detail.InquiryDetailDto;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @Operation(summary = "문의 답변", description = "문의에 답변을 추가하는 api 입니다.")
    @PostMapping("/comment")
    public ResponseEntity<CommonResponseDto> addInquiryComment(
            @AuthenticationPrincipal User user,
            @RequestBody AddInquiryCommentReqDto addInquiryCommentReqDto
    ) {
        log.debug("[INQUIRY_PUBLIC] 문의 답변 추가 요청이 들어왔습니다.");
        inquiryService.addInquiryComment(user, addInquiryCommentReqDto);
        log.debug("[INQUIRY_PUBLIC] 성공적으로 문의 답변을 추가하였습니다.");

        return ResponseEntity.ok(CommonResponseDto.createSuccessResponse("문의 답변을 추가했습니다."));
    }

    @Operation(summary = "문의 목록 조회", description = "작성한 문의 목록을 조회하는 api 입니다.")
    @GetMapping("/comment")
    public ResponseEntity<GetInquiryListResDto> getInquiryList(
            @AuthenticationPrincipal User user,
            @RequestParam int page
    ) {
        log.debug("[INQUIRY_PUBLIC] 작성 문의 조회 요청이 들어왔습니다.");
        List<GetInquiryDto> getInquiryDtoList = inquiryService.getInquiryList(user, page);
        log.debug("[INQUIRY_PUBLIC] 성공적으로 문의를 조회했습니다.");

        return ResponseEntity.ok(GetInquiryListResDto.successResponse("유저가 작성한 문의를 불러왔습니다.", getInquiryDtoList));
    }

    @Operation(summary = "문의 조회", description = "작성한 문의를 조회하는 api 입니다.")
    @GetMapping("/{inquiryCid}")
    public ResponseEntity<GetInquiryDetailResDto> getInquiryDetail(
            @AuthenticationPrincipal User user,
            @PathVariable Long inquiryCid
    ) {
        log.debug("[INQUIRY_PUBLIC] 작성 문의 조회 요청이 들어왔습니다.");
        InquiryDetailDto inquiryDetailDto = inquiryService.getInquiryDetail(user, inquiryCid);
        log.debug("[INQUIRY_PUBLIC] 성공적으로 문의를 조회했습니다.");

        return ResponseEntity.ok(GetInquiryDetailResDto.successResponse("문의를 불러왔습니다.", inquiryDetailDto));
    }
}
