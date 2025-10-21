package com.hunch.realestate.web.controller.pages;

import com.hunch.realestate.common.enums.PropertyType;
import com.hunch.realestate.common.enums.TransactionType;
import com.hunch.realestate.domain.dto.PagingResult;
import com.hunch.realestate.domain.dto.PropertyDTO;
import com.hunch.realestate.service.PropertyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 매물 관리 페이지 컨트롤러
 */
@Slf4j
@Controller
@RequestMapping("/pages/admin/properties")
@RequiredArgsConstructor
public class PropertyController {

    private final PropertyService propertyService;

    /**
     * 매물 목록 페이지
     */
    @GetMapping
    public String listProperties(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) PropertyType type,
            Model model) {

        try {
            PagingResult<PropertyDTO> result = propertyService.getProperties(type, page, size);
            
            model.addAttribute("properties", result);
            model.addAttribute("propertyTypes", PropertyType.values());
            model.addAttribute("currentType", type);
            model.addAttribute("currentPage", page);
            model.addAttribute("currentMenu", "properties-list");
            model.addAttribute("pageTitle", "매물 목록");
            
            return "pages/admin/properties/list";
        } catch (Exception e) {
            log.error("매물 목록 조회 중 오류 발생: ", e);
            model.addAttribute("errorMessage", "매물 목록을 불러오는데 실패했습니다.");
            return "pages/admin/properties/list";
        }
    }

    /**
     * 매물 등록 폼
     */
    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        if (!model.containsAttribute("property")) {
            model.addAttribute("property", new PropertyDTO());
        }
        
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
        try {
            PropertyDTO property = propertyService.getProperty(id);
            
            model.addAttribute("property", property);
            model.addAttribute("currentMenu", "properties-list");
            model.addAttribute("pageTitle", "매물 상세");
            
            return "pages/admin/properties/detail";
        } catch (Exception e) {
            log.error("매물 조회 중 오류 발생: ", e);
            model.addAttribute("errorMessage", "매물을 찾을 수 없습니다.");
            return "redirect:/pages/admin/properties";
        }
    }

    /**
     * 매물 수정 폼
     */
    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable String id, Model model) {
        try {
            if (!model.containsAttribute("property")) {
                PropertyDTO property = propertyService.getProperty(id);
                model.addAttribute("property", property);
            }
            
            model.addAttribute("propertyTypes", PropertyType.values());
            model.addAttribute("transactionTypes", TransactionType.values());
            model.addAttribute("currentMenu", "properties-list");
            model.addAttribute("pageTitle", "매물 수정");
            
            return "pages/admin/properties/edit";
        } catch (Exception e) {
            log.error("매물 조회 중 오류 발생: ", e);
            model.addAttribute("errorMessage", "매물을 찾을 수 없습니다.");
            return "redirect:/pages/admin/properties";
        }
    }
}
