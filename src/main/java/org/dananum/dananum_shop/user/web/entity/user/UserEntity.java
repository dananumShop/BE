package org.dananum.dananum_shop.user.web.entity.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.dananum.dananum_shop.global.web.entity.TimeEntity;
import org.dananum.dananum_shop.global.web.enums.user.AccountStatus;
import org.dananum.dananum_shop.global.web.enums.user.Roles;
import org.dananum.dananum_shop.user.web.dto.signup.SignupReqDto;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "user_table")
public class UserEntity extends TimeEntity {
    @Id
    @Column(name = "user_cid")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "유저 고유 아이디")
    private Long userCid;

    @NotNull
    @Schema(description = "유저 이메일(아이디)", example = "test@test.test")
    @Column(name = "user_email")
    private String userEmail;

    @NotNull
    @Schema(description = "유저 이름", example = "홍길동")
    @Column(name = "user_name")
    private String userName;

    @NotNull
    @Schema(description = "유저 비밀번호", example = "test")
    @Column(name = "user_password")
    private String userPassword;

    @NotNull
    @Schema(description = "계정 삭제 유무", example = "ACTIVE")
    @Enumerated(EnumType.STRING)
    private AccountStatus accountStatus;

    @Schema(description = "계정 탈퇴 날짜", example = "240101")
    @Column(name = "deleted_time")
    private LocalDateTime deletedTime;

    @NotNull
    @Schema(description = "유저 권한", example = "ROLE_USER")
    @Enumerated(EnumType.STRING)
    private Roles userRole;

    public static UserEntity from(final SignupReqDto signupInfo, final String password) {
        return UserEntity.builder()
                .userEmail(signupInfo.getUserEmail())
                .userName(signupInfo.getUserName())
                .userPassword(password)
                .accountStatus(AccountStatus.ACTIVE)
                .userRole(Roles.ROLE_USER)
                .build();
    }

    public UserEntity updateUserPassword(String newPwd) {
        this.userPassword = newPwd;
        return this;
    }

    public UserEntity updateUserName(String newName) {
        this.userName = newName;
        return this;
    }

    public UserEntity updateAccountStatus(AccountStatus accountStatus) {
        if(accountStatus == AccountStatus.ACTIVE) {
            this.accountStatus = AccountStatus.DELETED;
        }

        if(accountStatus == AccountStatus.DELETED) {
            this.accountStatus = AccountStatus.ACTIVE;
        }

        return this;
    }

}
