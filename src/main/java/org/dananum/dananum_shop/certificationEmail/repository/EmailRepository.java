package org.dananum.dananum_shop.certificationEmail.repository;

import org.dananum.dananum_shop.certificationEmail.web.entity.EmailEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmailRepository extends CrudRepository<EmailEntity, String> {
    Optional<EmailEntity> findByUserEmail(String userEmail);
}
