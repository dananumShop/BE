package org.dananum.dananum_shop.user.service;

import lombok.RequiredArgsConstructor;
import org.dananum.dananum_shop.user.repository.UserRepository;
import org.dananum.dananum_shop.user.util.UserValidation;
import org.dananum.dananum_shop.user.web.dto.SignupReqDto;
import org.dananum.dananum_shop.user.web.entity.user.UserEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;

    private final UserValidation userValidation;
    private final PasswordEncoder passwordEncoder;


    /**
     * 기능 - 회원가입
     * @params signupInfo
     *
     * return void
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
}
