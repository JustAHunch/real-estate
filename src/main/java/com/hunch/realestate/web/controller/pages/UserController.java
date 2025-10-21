package com.hunch.realestate.web.controller.pages;

import com.hunch.realestate.domain.dto.UserDTO;
import com.hunch.realestate.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * 사용자 관리 페이지 컨트롤러
 */
@Slf4j
@Controller
@RequestMapping("/pages/admin/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * 사용자 목록 페이지
     */
    @GetMapping
    public String listUsers(Model model) {
        try {
            List<UserDTO> users = userService.getAllUsers();
            model.addAttribute("users", users);
            model.addAttribute("currentMenu", "users-list");
            model.addAttribute("pageTitle", "사용자 목록");
            return "pages/admin/users/list";
        } catch (Exception e) {
            log.error("사용자 목록 조회 중 오류 발생: ", e);
            model.addAttribute("errorMessage", "사용자 목록을 불러오는데 실패했습니다.");
            return "pages/admin/users/list";
        }
    }

    /**
     * 사용자 등록 폼
     */
    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        if (!model.containsAttribute("user")) {
            model.addAttribute("user", new UserDTO());
        }
        model.addAttribute("currentMenu", "users-register");
        model.addAttribute("pageTitle", "사용자 등록");
        return "pages/admin/users/register";
    }

    /**
     * 사용자 상세 조회 페이지
     */
    @GetMapping("/{id}")
    public String showUserDetail(@PathVariable String id, Model model) {
        try {
            UserDTO user = userService.getUserById(id);
            model.addAttribute("user", user);
            model.addAttribute("currentMenu", "users-list");
            model.addAttribute("pageTitle", "사용자 상세");
            return "pages/admin/users/detail";
        } catch (Exception e) {
            log.error("사용자 조회 중 오류 발생: ", e);
            model.addAttribute("errorMessage", "사용자를 찾을 수 없습니다.");
            return "redirect:/pages/admin/users";
        }
    }

    /**
     * 사용자 수정 폼
     */
    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable String id, Model model) {
        try {
            if (!model.containsAttribute("user")) {
                UserDTO user = userService.getUserById(id);
                model.addAttribute("user", user);
            }
            model.addAttribute("currentMenu", "users-list");
            model.addAttribute("pageTitle", "사용자 수정");
            return "pages/admin/users/edit";
        } catch (Exception e) {
            log.error("사용자 조회 중 오류 발생: ", e);
            model.addAttribute("errorMessage", "사용자를 찾을 수 없습니다.");
            return "redirect:/pages/admin/users";
        }
    }
}
