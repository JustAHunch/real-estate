package com.hunch.realestate.domain.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private String id;
    private String username;
    private String password;
    private String email;
    private String name;
    private String phone;
    private List<String> roles;
    private Boolean isEnabled;
    private Boolean isLocked;
    private LocalDateTime lastLoginAt;
    private Integer failedLoginAttempts;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    /**
     * 유효성 검사
     */
    public Map<String, String> validate() {
        Map<String, String> errors = new HashMap<>();

        if (username == null || username.trim().isEmpty()) {
            errors.put("username", "사용자명을 입력해주세요");
        } else if (username.length() < 3) {
            errors.put("username", "사용자명은 3자 이상이어야 합니다");
        }

        // 신규 등록 시에만 비밀번호 필수
        if (id == null && (password == null || password.trim().isEmpty())) {
            errors.put("password", "비밀번호를 입력해주세요");
        }

        if (password != null && !password.trim().isEmpty() && password.length() < 4) {
            errors.put("password", "비밀번호는 4자 이상이어야 합니다");
        }

        if (email != null && !email.trim().isEmpty() && !isValidEmail(email)) {
            errors.put("email", "올바른 이메일 형식이 아닙니다");
        }

        return errors;
    }

    /**
     * 이메일 형식 검증
     */
    private boolean isValidEmail(String email) {
        return email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    }

    /**
     * 관리자 여부 확인
     */
    public boolean isAdmin() {
        return roles != null && (roles.contains("ROLE_ADMIN") || roles.contains("ADMIN"));
    }
}
