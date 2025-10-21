package com.hunch.realestate.web.controller.pages;

import com.hunch.realestate.domain.dto.CompanyInfoDTO;
import com.hunch.realestate.service.CompanyInfoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 부동산 정보 관리 페이지 컨트롤러
 */
@Slf4j
@Controller
@RequestMapping("/pages/admin/company")
@RequiredArgsConstructor
public class CompanyController {

    private final CompanyInfoService companyInfoService;

    /**
     * 부동산 정보 관리 페이지
     */
    @GetMapping("/info")
    public String companyInfo(Model model) {
        try {
            CompanyInfoDTO companyInfo = companyInfoService.getCompanyInfo();
            model.addAttribute("companyInfo", companyInfo);
            model.addAttribute("currentMenu", "company-info");
            model.addAttribute("pageTitle", "부동산정보 관리");
            return "pages/admin/company/company-info";
        } catch (Exception e) {
            log.error("부동산 정보 조회 중 오류 발생: ", e);
            model.addAttribute("errorMessage", "부동산 정보를 불러오는데 실패했습니다.");
            return "pages/admin/company/company-info";
        }
    }
}
