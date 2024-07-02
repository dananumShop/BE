package org.dananum.dananum_shop.user.repository;

import org.dananum.dananum_shop.user.web.entity.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Boolean existsByUserEmail(String userEmail);

    Optional<UserEntity> findByUserEmail(String userEmail);
}
