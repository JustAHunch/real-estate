package com.hunch.realestate.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

/**
 * 사용자 엔티티
 */
@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User extends BaseEntity {

    /**
     * 사용자명 (로그인 ID)
     */
    @Column(name = "username", nullable = false, unique = true, length = 100)
    private String username;

    /**
     * 비밀번호 (암호화되어 저장)
     */
    @Column(name = "password", nullable = false, length = 255)
    private String password;

    /**
     * 이메일
     */
    @Column(name = "email", length = 200)
    private String email;

    /**
     * 이름
     */
    @Column(name = "name", length = 100)
    private String name;

    /**
     * 전화번호
     */
    @Column(name = "phone", length = 50)
    private String phone;

    /**
     * 계정 활성화 여부
     */
    @Column(name = "is_enabled")
    @Builder.Default
    private Boolean isEnabled = true;

    /**
     * 계정 잠금 여부
     */
    @Column(name = "is_locked")
    @Builder.Default
    private Boolean isLocked = false;

    /**
     * 권한 목록
     */
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id")
    )
    @Column(name = "role")
    @Builder.Default
    private Set<String> roles = new HashSet<>();

    /**
     * 마지막 로그인 시간
     */
    @Column(name = "last_login_at")
    private java.time.LocalDateTime lastLoginAt;

    /**
     * 로그인 실패 횟수
     */
    @Column(name = "failed_login_attempts")
    @Builder.Default
    private Integer failedLoginAttempts = 0;

    /**
     * 권한 추가
     */
    public void addRole(String role) {
        if (this.roles == null) {
            this.roles = new HashSet<>();
        }
        this.roles.add(role);
    }

    /**
     * 권한 제거
     */
    public void removeRole(String role) {
        if (this.roles != null) {
            this.roles.remove(role);
        }
    }

    /**
     * 특정 권한 보유 여부 확인
     */
    public boolean hasRole(String role) {
        return this.roles != null && this.roles.contains(role);
    }

    /**
     * 로그인 성공 처리
     */
    public void onLoginSuccess() {
        this.lastLoginAt = java.time.LocalDateTime.now();
        this.failedLoginAttempts = 0;
    }

    /**
     * 로그인 실패 처리
     */
    public void onLoginFailure() {
        this.failedLoginAttempts++;
        if (this.failedLoginAttempts >= 5) {
            this.isLocked = true;
        }
    }
}
