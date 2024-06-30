package org.dananum.dananum_shop.user.web.entity.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import org.dananum.dananum_shop.global.web.entity.TimeEntity;

@Entity
@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Table(name = "user_profile_table")
public class UserProfileEntity extends TimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "유저 프로필 고유 아이디")
    @Column(name = "user_profile_cid")
    private Long userProfileCid;

    @Column(name = "user_profile_file_name")
    @Schema(description = "유저 프로필 이미지파일 이름")
    private String userProfileFileName;

    @Column(name = "user_profile_file_path")
    @Schema(description = "유저 프로필 이미지파일 경로")
    private String userProfileFilePath;
}
