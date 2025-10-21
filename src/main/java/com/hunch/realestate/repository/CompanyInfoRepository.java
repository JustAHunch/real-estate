package com.hunch.realestate.repository;

import com.hunch.realestate.domain.entity.CompanyInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 부동산 정보 Repository
 */
@Repository
public interface CompanyInfoRepository extends JpaRepository<CompanyInfo, String> {

    /**
     * 첫 번째 부동산 정보 조회 (단일 레코드 관리)
     */
    Optional<CompanyInfo> findFirstByOrderByCreatedAtDesc();
}
