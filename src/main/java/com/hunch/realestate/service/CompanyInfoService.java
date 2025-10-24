package com.hunch.realestate.service;

import com.hunch.realestate.domain.dto.CompanyInfoDTO;
import com.hunch.realestate.domain.entity.CompanyInfo;
import com.hunch.realestate.repository.CompanyInfoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 부동산 정보 관리 서비스
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CompanyInfoService {

    private final CompanyInfoRepository companyInfoRepository;

    /**
     * 부동산 정보 조회
     */
    public CompanyInfoDTO getCompanyInfo() {
        try {
            CompanyInfo companyInfo = companyInfoRepository.findFirstByOrderByCreatedAtDesc()
                    .orElse(null);

            if (companyInfo == null) {
                log.warn("등록된 부동산 정보가 없습니다.");
                return null;
            }

            return CompanyInfoDTO.builder()
                    .businessName(companyInfo.getBusinessName())
                    .address1(companyInfo.getAddress1())
                    .address2(companyInfo.getAddress2())
                    .directions(companyInfo.getDirections())
                    .phoneNumber(companyInfo.getPhoneNumber())
                    .faxNumber(companyInfo.getFaxNumber())
                    .managerPosition(companyInfo.getManagerPosition())
                    .managerName(companyInfo.getManagerName())
                    .managerPhone(companyInfo.getManagerPhone())
                    .managerEmail(companyInfo.getManagerEmail())
                    .managerPhoto(companyInfo.getManagerPhoto())
                    .build();
        } catch (Exception e) {
            log.error("부동산 정보 조회 중 오류 발생: ", e);
            return null;
        }
    }

    /**
     * Entity를 DTO로 변환
     */
    private CompanyInfoDTO convertToDTO(CompanyInfo entity) {
        return CompanyInfoDTO.builder()
                .businessName(entity.getBusinessName())
                .address1(entity.getAddress1())
                .address2(entity.getAddress2())
                .directions(entity.getDirections())
                .phoneNumber(entity.getPhoneNumber())
                .faxNumber(entity.getFaxNumber())
                .managerPosition(entity.getManagerPosition())
                .managerName(entity.getManagerName())
                .managerPhone(entity.getManagerPhone())
                .managerEmail(entity.getManagerEmail())
                .managerPhoto(entity.getManagerPhoto())
                .build();
    }

    /**
     * 부동산 정보 저장
     */
    @Transactional
    public void saveCompanyInfo(CompanyInfoDTO dto) {
        try {
            // 기존 데이터가 있으면 삭제 (단일 레코드 관리)
            companyInfoRepository.deleteAll();

            CompanyInfo entity = CompanyInfo.builder()
                    .businessName(dto.getBusinessName())
                    .address1(dto.getAddress1())
                    .address2(dto.getAddress2())
                    .directions(dto.getDirections())
                    .phoneNumber(dto.getPhoneNumber())
                    .faxNumber(dto.getFaxNumber())
                    .managerPosition(dto.getManagerPosition())
                    .managerName(dto.getManagerName())
                    .managerPhone(dto.getManagerPhone())
                    .managerEmail(dto.getManagerEmail())
                    .managerPhoto(dto.getManagerPhoto())
                    .build();

            companyInfoRepository.save(entity);
            log.info("부동산 정보 저장 완료: {}", dto.getBusinessName());
        } catch (Exception e) {
            log.error("부동산 정보 저장 중 오류 발생: ", e);
            throw new RuntimeException("부동산 정보 저장에 실패했습니다.", e);
        }
    }

    /**
     * 부동산 정보 수정
     */
    @Transactional
    public void updateCompanyInfo(CompanyInfoDTO companyInfo) {
        try {
            log.info("부동산 정보 수정: {}", companyInfo.getBusinessName());
            saveCompanyInfo(companyInfo);
        } catch (Exception e) {
            log.error("부동산 정보 수정 중 오류 발생: ", e);
            throw new RuntimeException("부동산 정보 수정에 실패했습니다.", e);
        }
    }
}
