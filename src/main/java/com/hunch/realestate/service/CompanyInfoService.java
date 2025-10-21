package com.hunch.realestate.service;

import com.hunch.realestate.domain.dto.CompanyInfoDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 부동산 정보 관리 서비스
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CompanyInfoService {

    /**
     * 부동산 정보 조회
     */
    public CompanyInfoDTO getCompanyInfo() {
        try {
            // JSON 파일에서 데이터 읽어오기 (파일이 있다면)
            // 현재는 기본값 반환
            return CompanyInfoDTO.builder()
                    .businessName("범부동산중개사무소")
                    .address1("경남 창원시 성산구 대원동 36-5")
                    .address2("경남 창원시 성산구 대원로 87 센트럴스페어 104호 농협은행앞")
                    .directions("대원초등학교 도보 1분 센트럴스페어 104호 농협은행앞")
                    .phoneNumber("055-237-1239")
                    .managerPosition("대표(CEO)")
                    .managerName("홍길동")
                    .managerPhone("010-6565-9400")
                    .managerPhoto("")
                    .build();
        } catch (Exception e) {
            log.error("부동산 정보 조회 중 오류 발생: ", e);
            return new CompanyInfoDTO();
        }
    }

    /**
     * 부동산 정보 저장
     */
    public void saveCompanyInfo(CompanyInfoDTO companyInfo) {
        try {
            // JSON 파일로 저장 (향후 구현)
            log.info("부동산 정보 저장: {}", companyInfo.getBusinessName());
            // jsonStorageService.save("company-info", companyInfo);
        } catch (Exception e) {
            log.error("부동산 정보 저장 중 오류 발생: ", e);
            throw new RuntimeException("부동산 정보 저장에 실패했습니다.", e);
        }
    }

    /**
     * 부동산 정보 수정
     */
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
