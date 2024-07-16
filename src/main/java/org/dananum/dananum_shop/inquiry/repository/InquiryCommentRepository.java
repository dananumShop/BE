package org.dananum.dananum_shop.inquiry.repository;

import org.dananum.dananum_shop.inquiry.web.entity.InquiryCommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InquiryCommentRepository extends JpaRepository<InquiryCommentEntity, Long> {
    List<InquiryCommentEntity> findAllByInquiryCid(Long inquiryCid);

}
