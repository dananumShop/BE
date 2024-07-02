package org.dananum.dananum_shop.user.service;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dananum.dananum_shop.global.config.security.TokenProvider;
import org.dananum.dananum_shop.global.web.advice.exception.CustomNotFoundException;
import org.dananum.dananum_shop.global.web.dto.TokenDto;
import org.dananum.dananum_shop.user.repository.UserRepository;
import org.dananum.dananum_shop.user.util.UserValidation;
import org.dananum.dananum_shop.user.web.dto.login.LoginReqDto;
import org.dananum.dananum_shop.user.web.dto.signup.SignupReqDto;
import org.dananum.dananum_shop.user.web.entity.user.UserEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserRepository userRepository;

    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final TokenProvider tokenProvider;
    private final UserValidation userValidation;
    private final PasswordEncoder passwordEncoder;


    /**
     * 기능 - 회원가입
     * @params signupInfo
     *
     * @return void
     */
    @Transactional
    public void signup(SignupReqDto signupInfo) {
        emailDuplicateTest(signupInfo.getUserEmail());

        String password = passwordEncoder.encode(signupInfo.getUserPassword());

        UserEntity signupUser = UserEntity.from(signupInfo, password);
        userRepository.save(signupUser);
    }

    /**
     * 기능 - 이메일 중복 테스트
     * @param email
     */
    public void emailDuplicateTest(String email) {
        userValidation.validateDuplicateEmail(email);
    }

    /**
     * 기능 - 로그인
     * @params loginInfo
     *
     * @return accessToken
     */
    @Transactional
    public void login(LoginReqDto loginInfo, HttpServletResponse httpServletResponse) {
        userValidation.validateLogin(loginInfo);

        createToken(loginInfo, httpServletResponse);
    }

    // 토큰 생성
    private void createToken(LoginReqDto loginInfo, HttpServletResponse httpServletResponse) {
        UsernamePasswordAuthenticationToken authenticationToken = loginInfo.toAuthentication();
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        tokenProvider.generateTokenDto(authentication, httpServletResponse);
    }


    public void test(LoginReqDto loginInfo) {
        UserEntity user = userRepository.findByUserEmail(loginInfo.getUserEmail())
                .orElseThrow(()->new CustomNotFoundException("테스트 로그인에 실패했습니다."));

        log.info(user.getUserEmail());
        log.info(user.getUserPassword());
        log.info(String.valueOf(user.getUserCid()));
    }
}
