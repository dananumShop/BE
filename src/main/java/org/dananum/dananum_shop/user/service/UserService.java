package org.dananum.dananum_shop.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dananum.dananum_shop.global.web.advice.exception.CustomDataIntegrityViolationException;
import org.dananum.dananum_shop.global.web.enums.AccountStatus;
import org.dananum.dananum_shop.user.repository.UserRepository;
import org.dananum.dananum_shop.user.util.UserValidation;
import org.dananum.dananum_shop.user.web.dto.getUser.GetUserRoleResDto;
import org.dananum.dananum_shop.user.web.entity.user.UserEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final UserValidation userValidation;

    public UserService(UserRepository userRepository, UserValidation userValidation) {
        this.userRepository = userRepository;
        this.userValidation = userValidation;
    }

    /**
     * 사용자의 역할을 가져옵니다.
     *
     * @param user 현재 로그인한 사용자
     * @return 사용자의 역할을 담은 GetUserRoleResDto
     */
    @Transactional(readOnly = true)
    public GetUserRoleResDto getUserRole(User user) {
        UserEntity loggedInUser = userValidation.validateExistUser(user.getUsername());

        return GetUserRoleResDto.from(loggedInUser);
    }

    /**
     * 사용자의 계정을 탈퇴 처리합니다.
     *
     * @param user 현재 로그인한 사용자
     */
    public void accountCancellation(User user) {
        UserEntity loggedInUser = userValidation.validateExistUser(user.getUsername());
        if (loggedInUser.getAccountStatus() == AccountStatus.DELETED) {
            throw new CustomDataIntegrityViolationException("이미 탈퇴된 계정입니다.");
        }

        loggedInUser.updateAccountStatus(AccountStatus.DELETED);
        loggedInUser.setDeletedTime(LocalDateTime.now());
        userRepository.save(loggedInUser);
    }

    /**
     * 사용자의 계정을 복구합니다.
     *
     * @param user 현재 로그인한 사용자
     */
    public void accountRecovery(User user) {
        UserEntity loggedInUser = userValidation.validateExistUser(user.getUsername());
        if (loggedInUser.getAccountStatus() == AccountStatus.ACTIVE) {
            throw new CustomDataIntegrityViolationException("계정이 탈퇴 상태가 아닙니다");
        }

        checkIfRecoveryPeriodPassed(loggedInUser);

        loggedInUser.updateAccountStatus(AccountStatus.ACTIVE);
        loggedInUser.setDeletedTime(null);
        userRepository.save(loggedInUser);
    }

    /**
     * 계정 복구 가능 기간이 지났는지 확인합니다.
     *
     * @param user 복구하려는 사용자 엔티티
     */
    private void checkIfRecoveryPeriodPassed(UserEntity user) {
        LocalDateTime now = LocalDateTime.now();
        if (user.getDeletedTime() != null &&
                Duration.between(user.getDeletedTime(), now).toDays() >= 15) {
            throw new CustomDataIntegrityViolationException("복구 기간이 지났습니다");
        }
    }
}
