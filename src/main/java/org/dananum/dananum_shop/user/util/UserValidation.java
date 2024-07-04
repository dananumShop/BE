package org.dananum.dananum_shop.user.util;

import lombok.RequiredArgsConstructor;
import org.dananum.dananum_shop.global.web.advice.exception.CustomAccessDeniedException;
import org.dananum.dananum_shop.global.web.advice.exception.CustomDataIntegerityCiolationException;
import org.dananum.dananum_shop.global.web.advice.exception.CustomMissingFileException;
import org.dananum.dananum_shop.global.web.advice.exception.CustomNotFoundException;
import org.dananum.dananum_shop.global.web.enums.AccountStatus;
import org.dananum.dananum_shop.user.repository.UserRepository;
import org.dananum.dananum_shop.user.web.dto.login.LoginReqDto;
import org.dananum.dananum_shop.user.web.entity.user.UserEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
@RequiredArgsConstructor
public class UserValidation {
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    // 이메일 중복 확인
    public void validateDuplicateEmail(String email) {
        if (userRepository.existsByUserEmail(email)) {
            throw new CustomDataIntegerityCiolationException("이미 사용중인 이메일입니다.");
        }
    }

    // 로그인 가능 상태 확인
    public void validateLogin(LoginReqDto loginInfo) {
        UserEntity userEntity = userRepository.findByUserEmail(loginInfo.getUserEmail())
                .orElseThrow(() -> new CustomNotFoundException("일치하는 유저가 존재하지 않습니다."));

        if(userEntity.getAccountStatus() == AccountStatus.DELETED) {
            throw new CustomAccessDeniedException("탈퇴 처리된 유저입니다.");
        }

        if(!passwordEncoder.matches(loginInfo.getUserPassword(), userEntity.getUserPassword())){
            throw new BadCredentialsException("비밀번호가 일치하지 않습니다.");
        }
    }

    // 이메일 유저 검색
    public UserEntity findUserByUserEmail(String userEmail) {
        return userRepository.findByUserEmail(userEmail)
                .orElseThrow(()->new CustomNotFoundException("일치하는 이메일이 존재하지 않습니다."));
    }

    // 유저 유무 확인
    public UserEntity validateExistUser(String userEmail) {
        return userRepository.findByUserEmail(userEmail)
                .orElseThrow(()-> new CustomNotFoundException("로그인된 유저가 존재하지 않습니다."));
    }

}
