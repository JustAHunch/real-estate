package com.hunch.realestate.web.controller.pages;

import com.hunch.realestate.common.enums.PropertyType;
import com.hunch.realestate.common.enums.TransactionType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 매물 관리 페이지 컨트롤러 (화면 이동만 담당)
 * 데이터는 PropertyResource API를 통해 AJAX로 로드
 */
@Slf4j
@Controller
@RequestMapping("/admin/properties")
@RequiredArgsConstructor
public class PropertyController {

    /**
     * 매물 목록 페이지
     */
    @GetMapping("/list")
    public String listProperties(Model model) {
        model.addAttribute("propertyTypes", PropertyType.values());
        model.addAttribute("currentMenu", "properties-list");
        model.addAttribute("pageTitle", "매물 목록");

        return "pages/admin/properties/list";
    }

    /**
     * 매물 등록 폼
     */
    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("propertyTypes", PropertyType.values());
        model.addAttribute("transactionTypes", TransactionType.values());
        model.addAttribute("currentMenu", "properties-register");
        model.addAttribute("pageTitle", "매물 등록");

        return "pages/admin/properties/register";
    }

    /**
     * 매물 상세 조회 페이지
     */
    @GetMapping("/{id}")
    public String showPropertyDetail(@PathVariable String id, Model model) {
        model.addAttribute("propertyId", id);
        model.addAttribute("currentMenu", "properties-list");
        model.addAttribute("pageTitle", "매물 상세");

        return "pages/admin/properties/detail";
    }

    /**
     * 매물 수정 폼
     */
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable String id, Model model) {
        model.addAttribute("propertyId", id);
        model.addAttribute("propertyTypes", PropertyType.values());
        model.addAttribute("transactionTypes", TransactionType.values());
        model.addAttribute("currentMenu", "properties-list");
        model.addAttribute("pageTitle", "매물 수정");

        return "pages/admin/properties/edit";
    }
}
