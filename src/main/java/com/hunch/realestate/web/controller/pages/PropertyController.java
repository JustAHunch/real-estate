package com.hunch.realestate.web.controller.pages;

import com.hunch.realestate.common.enums.PropertyType;
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

@Slf4j
@Controller
@RequiredArgsConstructor
public class PropertyController {

    private final PropertyService propertyService;

    /**
     * 매물 목록 페이지
     */
    @RequestMapping("/pages/admin/properties")
    @GetMapping
    public String listProperties(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) PropertyType type,
            Model model) {

        PagingResult<PropertyDTO> result = propertyService.getProperties(type, page, size);
        model.addAttribute("properties", result);
        model.addAttribute("propertyTypes", PropertyType.values());
        return "pages/admin/properties/list";
    }

    /**
     * 매물 등록 폼
     */
    @GetMapping("/pages/admin/properties/register")
    public String showRegistrationForm(Model model) {
        if (!model.containsAttribute("property")) {
            model.addAttribute("property", new PropertyDTO());
        }
        model.addAttribute("propertyTypes", PropertyType.values());
        return "pages/admin/properties/register";
    }

    /**
     * 매물 상세 조회 페이지
     */
    @GetMapping("/pages/admin/properties/{id}")
    public String showPropertyDetail(@PathVariable Long id, Model model) {
        PropertyDTO property = propertyService.getProperty(id);
        model.addAttribute("property", property);
        return "pages/admin/properties/detail";
    }

    /**
     * 매물 수정 폼
     */
    @GetMapping("/pages/admin/properties/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        if (!model.containsAttribute("property")) {
            PropertyDTO property = propertyService.getProperty(id);
            model.addAttribute("property", property);
        }
        model.addAttribute("propertyTypes", PropertyType.values());
        return "pages/admin/properties/edit";
    }
}