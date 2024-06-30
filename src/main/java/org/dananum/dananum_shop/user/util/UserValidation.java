package org.dananum.dananum_shop.user.util;

import lombok.RequiredArgsConstructor;
import org.dananum.dananum_shop.global.web.advice.exception.CustomDataIntegerityCiolationException;
import org.dananum.dananum_shop.user.repository.UserRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserValidation {
    private final UserRepository userRepository;

    public void validateDuplicateEmail(String email) {
        if (userRepository.existsByUserEmail(email)) {
            throw new CustomDataIntegerityCiolationException("이미 사용중인 이메일입니다.");
        }
    }
}
