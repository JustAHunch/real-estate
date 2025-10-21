package com.hunch.realestate.web.controller.pages;

import com.hunch.realestate.service.PropertyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 관리자 페이지 컨트롤러
 */
@Controller
@RequiredArgsConstructor
public class AdminController {

    private final PropertyService propertyService;

    /**
     * 대시보드
     */
    @GetMapping({"/", "/index", "/pages/admin/dashboard"})
    public String dashboard(Model model) {
        // 메뉴 활성화
        model.addAttribute("currentMenu", "dashboard");
        model.addAttribute("pageTitle", "대시보드");

        // 관리자 정보
        model.addAttribute("adminName", "관리자");

        // 알림 정보
        model.addAttribute("notificationCount", 0);

        // 대시보드 통계 데이터
        try {
            // 매물 통계
            long totalProperties = propertyService.getTotalCount();
            model.addAttribute("totalProperties", totalProperties);
            
            // 추가 통계 데이터는 필요시 구현
            model.addAttribute("newPropertiesThisMonth", 0);
            model.addAttribute("soldPropertiesThisMonth", 0);
        } catch (Exception e) {
            model.addAttribute("totalProperties", 0);
            model.addAttribute("newPropertiesThisMonth", 0);
            model.addAttribute("soldPropertiesThisMonth", 0);
        }

        return "pages/admin/dashboard";
    }
}
