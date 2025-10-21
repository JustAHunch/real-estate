package com.hunch.realestate.web.controller.api;

import com.hunch.realestate.domain.dto.CompanyInfoDTO;
import com.hunch.realestate.service.CompanyInfoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 부동산 정보 관리 REST API 컨트롤러
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/admin/company")
@RequiredArgsConstructor
public class CompanyResource {

    private final CompanyInfoService companyInfoService;

    /**
     * 부동산 정보 조회
     */
    @GetMapping("/info")
    public ResponseEntity<CompanyInfoDTO> getCompanyInfo() {
        try {
            CompanyInfoDTO companyInfo = companyInfoService.getCompanyInfo();
            return ResponseEntity.ok(companyInfo);
        } catch (Exception e) {
            log.error("부동산 정보 조회 중 오류 발생: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * 부동산 정보 저장
     */
    @PostMapping("/info")
    public ResponseEntity<?> saveCompanyInfo(@RequestBody CompanyInfoDTO companyInfo) {
        // 유효성 검사
        Map<String, String> errors = companyInfo.validate();
        if (!errors.isEmpty()) {
            return ResponseEntity.badRequest().body(errors);
        }

        try {
            companyInfoService.saveCompanyInfo(companyInfo);
            return ResponseEntity.ok().body("부동산 정보가 저장되었습니다.");
        } catch (Exception e) {
            log.error("부동산 정보 저장 중 오류 발생: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("부동산 정보 저장 중 오류가 발생했습니다.");
        }
    }

    /**
     * 부동산 정보 수정
     */
    @PutMapping("/info")
    public ResponseEntity<?> updateCompanyInfo(@RequestBody CompanyInfoDTO companyInfo) {
        // 유효성 검사
        Map<String, String> errors = companyInfo.validate();
        if (!errors.isEmpty()) {
            return ResponseEntity.badRequest().body(errors);
        }

        try {
            companyInfoService.updateCompanyInfo(companyInfo);
            return ResponseEntity.ok().body("부동산 정보가 수정되었습니다.");
        } catch (Exception e) {
            log.error("부동산 정보 수정 중 오류 발생: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("부동산 정보 수정 중 오류가 발생했습니다.");
        }
    }
}
