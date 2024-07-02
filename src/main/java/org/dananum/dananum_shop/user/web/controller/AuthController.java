package org.dananum.dananum_shop.user.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dananum.dananum_shop.global.web.dto.CommonResponseDto;
import org.dananum.dananum_shop.user.service.AuthService;
import org.dananum.dananum_shop.user.web.dto.login.LoginReqDto;
import org.dananum.dananum_shop.user.web.dto.signup.SignupReqDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/public/auth")
@RequiredArgsConstructor
@Tag(name = "회원관련 API")
public class AuthController {
    private final AuthService authService;

    @Operation(summary = "회원가입", description = "회원가입을 다루는 api입니다.")
    @PostMapping("/signup")
    public ResponseEntity<CommonResponseDto> signup(@RequestBody SignupReqDto signupInfo) {
        log.debug("[AUTH] 회원가입 요청이 들어왔습니다. \n{}", signupInfo);
        authService.signup(signupInfo);
        log.debug("[AUTH] 회원가입이 성공적으로 이루어 졌습니다.");

        return ResponseEntity.status(HttpStatus.CREATED).body(CommonResponseDto.createSuccessResponse("회원가입 성공"));
    }

    @Operation(summary = "로그인", description = "로그인을 다루는 api입니다.")
    @PostMapping("/login")
    public ResponseEntity<CommonResponseDto> login(@RequestBody LoginReqDto loginInfo, HttpServletResponse httpServletResponse) {
        log.debug("[AUTH] 로그인 요청이 들어왔습니다. \n{}", loginInfo);
        authService.login(loginInfo, httpServletResponse);
        log.debug("[AUTH] 로그인이 성공적으로 이루어 졌습니다.");

        return ResponseEntity.ok(CommonResponseDto.successResponse("로그인 성공"));
    }

    @PostMapping("/test")
    public void test(@RequestBody LoginReqDto loginInfo) {
        log.debug("테스트 시작 \n{}", loginInfo);
        authService.test(loginInfo);
        log.debug("테스트 성공");

    }
}
