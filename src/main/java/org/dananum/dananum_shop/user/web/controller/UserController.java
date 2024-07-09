package org.dananum.dananum_shop.user.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dananum.dananum_shop.global.web.dto.CommonResponseDto;
import org.dananum.dananum_shop.user.service.UserService;
import org.dananum.dananum_shop.user.web.dto.edit.EditProfileReqDto;
import org.dananum.dananum_shop.user.web.dto.getUser.GetUserRoleResDto;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@Slf4j
@RequestMapping("/user")
@RequiredArgsConstructor
@Tag(name = "유저관련 API")
public class UserController {

    private final UserService userService;


    @Operation(summary = "유저 권한 조회", description = "이메일 권한 확인하기 위한 api입니다.")
    @GetMapping("/verification")
    public ResponseEntity<GetUserRoleResDto> getUserVerification(@AuthenticationPrincipal User user) {
        log.debug("[USER] 유저의 인증 상태 조회 요청이 들어왔습니다.");
        GetUserRoleResDto userVerification = userService.getUserRole(user);
        log.debug("[USER] 유저의 인증상태를 성공적으로 불러왔습니다.");

        return ResponseEntity.ok(userVerification);
    }

    @Operation(summary = "유저 계정 탈퇴", description = "회원 탈퇴를 위한 api입니다.")
    @PostMapping("/account-cancellation")
    public ResponseEntity<CommonResponseDto> accountCancellation(@AuthenticationPrincipal User user) {
        log.debug("[USER] 회원 탈퇴 요청이 들어왔습니다.");
        userService.accountCancellation(user);
        log.debug("[USER] 회원 탈퇴가 성공적으로 이루어졌습니다.");

        return ResponseEntity.ok(CommonResponseDto.successResponse("계정 탈퇴가 정상적으로 진행되었습니다."));
    }

    @Operation(summary = "유저 계정 복구", description = "회원 북구를 위한 api입니다.")
    @PostMapping("/account-recovery")
    public ResponseEntity<CommonResponseDto> accountRecovery(@AuthenticationPrincipal User user) {
        log.debug("[USER] 계정 복구 요청이 들어왔습니다.");
        userService.accountRecovery(user);
        log.debug("[USER] 계정 복구가 성공적으로 이루어졌습니다.");

        return ResponseEntity.ok(CommonResponseDto.successResponse("계정 복구가 정상적으로 진행되었습니다."));
    }

    @Operation(summary = "유저 정보 수정", description = "유저의 개인정보를 수정하는 api입니다.")
    @PutMapping(name = "/edit", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonResponseDto> editProfile(
            @AuthenticationPrincipal User user,
            @RequestPart(name = "editInfo") @Parameter(schema = @Schema(type = "string", format = "binary")) EditProfileReqDto editProfileReqDto,
            @RequestPart(name = "profileImage") MultipartFile profileImage
    ) {
        log.debug("[USER] 프로필 수정 요청이 들어왔습니다.");
        userService.editProfile(user, editProfileReqDto, profileImage);
        log.debug("[USER] 프로필 수정이 성공적으로 이루어졌습니다.");

        return ResponseEntity.ok(CommonResponseDto.successResponse("프로필 수정이 정상적으로 진행되었습니다."));
    }

    @Operation(summary = "프로필 이미지 삭제", description = "유저의 프로필 이미지를 삭제하는 api입니다.")
    @DeleteMapping("/profile-image")
    public ResponseEntity<CommonResponseDto> deleteProfileImg(
            @AuthenticationPrincipal User user
    ) {
        log.debug("[USER] 프로필 이미지 삭제 요청이 들어왔습니다.");
        userService.deleteProfileImage(user);
        log.debug("[USER] 프로필 이미지를 성공적으로 삭제했습니다.");

        return ResponseEntity.ok(CommonResponseDto.successResponse("프로필 이미지가 정상적으로 삭제되었습니다."));
    }
}
