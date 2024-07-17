package org.dananum.dananum_shop.global.util;

import lombok.RequiredArgsConstructor;
import org.dananum.dananum_shop.global.web.advice.exception.CustomAccessDeniedException;
import org.dananum.dananum_shop.global.web.advice.exception.CustomNotFoundException;
import org.dananum.dananum_shop.global.web.enums.user.Roles;
import org.dananum.dananum_shop.user.repository.UserRepository;
import org.dananum.dananum_shop.user.web.entity.user.UserEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminValidation {

    private final UserRepository userRepository;

    /**
     * 사용자가 관리자 권한을 가지고 있는지 검증하는 메서드입니다.
     *
     * @param user 검증할 사용자 정보
     * @return 검증된 사용자 엔티티
     * @throws CustomNotFoundException 일치하는 사용자를 찾을 수 없는 경우
     * @throws CustomAccessDeniedException 사용자가 관리자 권한이 아닌 경우
     */
    public UserEntity validateAdminUser(User user) {
        UserEntity userEntity = userRepository.findByUserEmail(user.getUsername())
                .orElseThrow(() -> new CustomNotFoundException("일치하는 유저가 없습니다."));

        if (userEntity.getUserRole().equals(Roles.ROLE_ADMIN)) {
            throw new CustomAccessDeniedException("관리자가 아닙니다");
        }

        return userEntity;
    }
}
