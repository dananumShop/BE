package org.dananum.dananum_shop.user.repository;

import org.dananum.dananum_shop.user.web.entity.user.UserEntity;
import org.dananum.dananum_shop.user.web.entity.user.UserProfileImgEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserProfileImgRepository extends JpaRepository<UserProfileImgEntity, Long> {
    Optional<UserProfileImgEntity> findByUserEntity(UserEntity user);
}
