package org.dananum.dananum_shop.inquiry.repository;

import org.dananum.dananum_shop.inquiry.web.entity.InquiryCommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InauiryCommentRepository extends JpaRepository<InquiryCommentEntity, Long> {
}
