package org.dananum.dananum_shop.global.web.entity.dataLoader;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dananum.dananum_shop.global.web.enums.AccountStatus;
import org.dananum.dananum_shop.global.web.enums.Roles;
import org.dananum.dananum_shop.user.repository.UserRepository;
import org.dananum.dananum_shop.user.web.entity.user.UserEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class CreateDefaultAdmin implements CommandLineRunner {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        createDefaultAdmin();
    }

    @Value("${spring.default.admin_id}")
    private String defaultUserId;
    @Value("${spring.default.admin_pwd}")
    private String defaultPassword;

    private void createDefaultAdmin(){
        final String defaultUserName = "관리자";

        if(!userRepository.existsByUserEmail(defaultUserId)){
            UserEntity defaultUser = UserEntity.builder()
                    .userEmail(defaultUserId)
                    .userPassword(passwordEncoder.encode(defaultPassword))
                    .userName(defaultUserName)
                    .userRole(Roles.ROLE_ADMIN)
                    .accountStatus(AccountStatus.ACTIVE)
                    .build();

            log.info("[CREATE_DEFAULT_ADMIN] 관리자 계정이 생성되었습니다.");

            userRepository.save(defaultUser);
        } else {
            log.info("[CREATE_DEFAULT_ADMIN] 이미 관리자 계정이 존재하여 생성하지 않았습니다.");
        }

    }
}