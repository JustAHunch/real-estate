package com.hunch.realestate.web.controller.pages;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 부동산 정보 관리 페이지 컨트롤러
 * 데이터는 CompanyResource API를 통해 AJAX로 로드
 */
@Slf4j
@Controller
@RequestMapping("/admin/company")
@RequiredArgsConstructor
public class CompanyController {

    /**
     * 부동산 정보 관리 페이지
     */
    @GetMapping("/info")
    public String companyInfo(Model model) {
        model.addAttribute("currentMenu", "company-info");
        model.addAttribute("pageTitle", "부동산정보 관리");
        return "pages/admin/company/company-info";
    }
}
