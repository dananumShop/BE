package org.dananum.dananum_shop.inquiry.repository;

import org.dananum.dananum_shop.global.web.enums.inquiry.InquiryStatus;
import org.dananum.dananum_shop.inquiry.web.entity.InquiryEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InquiryRepository extends JpaRepository<InquiryEntity, Long> {

    Page<InquiryEntity> findAllByUserCid(Long userCid, Pageable pageable);

    Page<InquiryEntity> findAllByInquiryStatus(Pageable pageable, InquiryStatus inquiryStatus);
}
