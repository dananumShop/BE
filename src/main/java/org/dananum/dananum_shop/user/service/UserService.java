package org.dananum.dananum_shop.user.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dananum.dananum_shop.global.aws.ImageUploadService;
import org.dananum.dananum_shop.global.config.security.TokenProvider;
import org.dananum.dananum_shop.global.web.advice.exception.CustomDataIntegrityViolationException;
import org.dananum.dananum_shop.global.web.advice.exception.CustomNotFoundException;
import org.dananum.dananum_shop.global.web.enums.user.AccountStatus;
import org.dananum.dananum_shop.user.repository.UserProfileImgRepository;
import org.dananum.dananum_shop.user.repository.UserRepository;
import org.dananum.dananum_shop.user.util.UserValidation;
import org.dananum.dananum_shop.user.web.dto.edit.EditProfileReqDto;
import org.dananum.dananum_shop.user.web.dto.getUser.GetUserRoleResDto;
import org.dananum.dananum_shop.user.web.dto.getUser.UserInfoDto;
import org.dananum.dananum_shop.user.web.entity.user.UserEntity;
import org.dananum.dananum_shop.user.web.entity.user.UserProfileImgEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.Duration;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final UserProfileImgRepository userProfileImgRepository;
    private final UserValidation userValidation;
    private final TokenProvider tokenProvider;

    private final ImageUploadService imageUploadService;

    private final PasswordEncoder passwordEncoder;

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

    /**
     * 사용자의 프로필을 수정합니다.
     *
     * @param user 현재 로그인한 사용자
     * @param editProfileReqDto 프로필 수정 요청 DTO
     * @param profileImage 새로운 프로필 이미지 파일
     */
    @Transactional
    public void editProfile(User user, EditProfileReqDto editProfileReqDto, MultipartFile profileImage) {
        UserEntity userEntity = userValidation.validateExistUser(user.getUsername());

        if(!editProfileReqDto.getUserPassword().isEmpty()) {
            String newPwd = passwordEncoder.encode(editProfileReqDto.getUserPassword());
            userEntity.updateUserPassword(newPwd);
        }

        if(!editProfileReqDto.getUserName().isEmpty()) {
            userEntity.updateUserName(editProfileReqDto.getUserName());
        }

        if(!profileImage.isEmpty()) {
            String oldImagePath = editProfileImage(userEntity, profileImage);

            deleteProfileImagesFromS3(oldImagePath);
        }
    }

    /**
     * 사용자의 프로필 이미지를 수정합니다.
     *
     * @param user 현재 로그인한 사용자 엔티티
     * @param profileImage 새로운 프로필 이미지 파일
     * @return 이전 프로필 이미지의 파일 경로
     */
    private String editProfileImage(UserEntity user, MultipartFile profileImage) {
        UserProfileImgEntity oldProfileImg = userProfileImgRepository.findByUserEntity(user)
                .orElse(null);

        if(oldProfileImg != null) {
            userProfileImgRepository.delete(oldProfileImg);
        }

        imageUploadService.uploadProfileImg(profileImage, "profile_img", user);

        return oldProfileImg.getUserProfileFilePath();
    }

    /**
     * S3에서 이미지를 비동기적으로 삭제하는 메서드
     *
     * @param imagePath 삭제할 이미지 엔티티 리스트
     */
    @Async
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void deleteProfileImagesFromS3(String imagePath) {
            imageUploadService.deleteImage(imagePath);
    }

    /**
     * 사용자의 프로필 이미지를 삭제합니다.
     *
     * @param user 현재 로그인한 사용자 엔티티
     */
    public void deleteProfileImage(User user) {
        UserEntity userEntity = userValidation.validateExistUser(user.getUsername());

        UserProfileImgEntity oldProfileImg = userProfileImgRepository.findByUserEntity(userEntity)
                .orElseThrow(() -> new CustomNotFoundException("프로필 이미지가 없습니다."));

        userProfileImgRepository.delete(oldProfileImg);
    }

    /**
     * 유저의 정보를 불러옵니다.
     *
     * @param user 현재 로그인한 사용자 엔티티
     * @return UserInfoDto 유저의 정보를 담은 dto
     */
    public UserInfoDto getUserInfo(User user) {
        UserEntity userEntity = userValidation.validateExistUser(user.getUsername());

        UserProfileImgEntity userProfile = userProfileImgRepository.findByUserEntity(userEntity)
                .orElse(null);

        return UserInfoDto.from(userEntity, userProfile);
    }

    /**
     * 현재 사용자의 Access Token을 갱신하여 새로운 Access Token을 발급하는 메서드입니다.
     * HttpServletRequest에서 Access Token을 추출하고,
     * 추출한 Access Token을 사용하여 Refresh Token을 검증하고,
     * 유효한 경우 해당 유저의 Authentication 정보를 가져와서 새로운 Access Token을 생성합니다.
     * 생성된 Access Token을 HttpServletResponse의 Authorization 헤더에 설정하여 반환합니다.
     *
     * @param httpServletRequest  현재 HTTP 요청 객체
     * @param httpServletResponse HTTP 응답 객체
     */
    public void getNewAccessToken(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        tokenProvider.getNewAccessToken(httpServletRequest, httpServletResponse);
    }
}
