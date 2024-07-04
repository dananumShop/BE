package org.dananum.dananum_shop.user.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dananum.dananum_shop.global.web.dto.CommonResponseDto;
import org.dananum.dananum_shop.user.service.UserService;
import org.dananum.dananum_shop.user.web.dto.getUser.GetUserVerificationDto;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/user")
@RequiredArgsConstructor
@Tag(name = "유저관련 API")
public class UserController {

    private final UserService userService;

    @Operation(summary = "유저 이메일 인증상태 조회", description = "이메일 인증상태를 확인하기 위한 api입니다.")
    @GetMapping("/verification")
    public ResponseEntity<GetUserVerificationDto> getUserVerification(@AuthenticationPrincipal User user) {
        log.debug("[USER] 유저의 인증 상태 조회 요청이 들어왔습니다.");
        GetUserVerificationDto userVerification = userService.getUserVerification(user);
        log.debug("[USER] 유저의 인증상태를 성공적으로 불러왔습니다.");

        return ResponseEntity.ok(userVerification);
    }

    @Operation(summary = "유저 계정 탈퇴", description = "회원 탈퇴를 위한 api입니다.")
    @PostMapping("/account-cancellation")
    public ResponseEntity<CommonResponseDto> accountCancellation(@AuthenticationPrincipal User user) {
        log.debug("[USER] 회원 탈퇴 요청이 들어왔습니다.");
        userService.updateAccountCancellation(user);
        log.debug("[USER] 회원 탈퇴가 성공적으로 이루어졌습니다.");

        return ResponseEntity.ok(CommonResponseDto.successResponse("계정 탈퇴가 정상적으로 진행되었습니다."));
    }

}
