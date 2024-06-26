package org.dananum.dananum_shop.user.web.entity.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import org.dananum.dananum_shop.global.web.entity.TimeEntity;
import org.dananum.dananum_shop.global.web.enums.AccountStatus;
import org.dananum.dananum_shop.global.web.enums.Roles;

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

    @Schema(description = "유저 장바구니", example = "1")
    @Column(name = "user_cart")
    private Long userCart;

    @Schema(description = "유저 이메일(아이디)", example = "test@test.test")
    @Column(name = "user_email")
    private String userEmail;

    @Schema(description = "유저 이름", example = "홍길동")
    @Column(name = "user_name")
    private String userName;

    @Schema(description = "유저 비밀번호", example = "test")
    @Column(name = "user_password")
    private String userPassword;

    @Schema(description = "계정 삭제 유무", example = "ACTIVE")
    @Column(name = "account_status")
    private AccountStatus accountStatus;

    @Schema(description = "유저 권한", example = "ROLE_USER")
    @Column(name = "user_role")
    private Roles userRole;

}
