package com.hunch.realestate.web.controller.pages;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminController {

    @GetMapping({"/", "/index", "/pages/admin/dashboard"})
    public String dashboard(Model model) {
        // 메뉴 활성화
        model.addAttribute("currentMenu", "dashboard");
        model.addAttribute("pageTitle", "대시보드");

        // 관리자 정보
        model.addAttribute("adminName", "관리자명");

        // 알림 정보
        model.addAttribute("notificationCount", 15);
//        model.addAttribute("notifications", getNotifications());

        // 대시보드 데이터
//        model.addAttribute("orderCount", orderService.getNewOrderCount());
//        model.addAttribute("orders", orderService.getRecentOrders());

        return "pages/admin/dashboard";
    }
}