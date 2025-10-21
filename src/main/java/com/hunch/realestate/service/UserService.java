package com.hunch.realestate.service;

import com.hunch.realestate.domain.dto.UserDTO;
import com.hunch.realestate.domain.entity.User;
import com.hunch.realestate.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 사용자 관리 서비스
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    
    /**
     * 애플리케이션 시작 시 기본 관리자 계정 생성
     */
    @EventListener(ApplicationReadyEvent.class)
    @Transactional
    public void initializeDefaultUser() {
        if (!userRepository.existsByUsername("admin")) {
            User admin = User.builder()
                    .username("admin")
                    .password(passwordEncoder.encode("admin123"))
                    .name("관리자")
                    .email("admin@realestate.com")
                    .roles(Set.of("ROLE_ADMIN", "ROLE_USER"))
                    .isEnabled(true)
                    .isLocked(false)
                    .build();
            
            userRepository.save(admin);
            log.info("기본 관리자 계정이 생성되었습니다. (username: admin, password: admin123)");
        }
    }

    /**
     * 전체 사용자 목록 조회
     */
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * 사용자 ID로 조회
     */
    public UserDTO getUserById(String id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다: " + id));
        return convertToDTO(user);
    }

    /**
     * 사용자명으로 조회
     */
    public UserDTO getUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다: " + username));
        return convertToDTO(user);
    }

    /**
     * 사용자 등록
     */
    @Transactional
    public void registerUser(UserDTO userDTO) {
        // 중복 확인
        if (userRepository.existsByUsername(userDTO.getUsername())) {
            throw new RuntimeException("이미 존재하는 사용자명입니다.");
        }

        if (userDTO.getEmail() != null && userRepository.existsByEmail(userDTO.getEmail())) {
            throw new RuntimeException("이미 존재하는 이메일입니다.");
        }

        User user = User.builder()
                .username(userDTO.getUsername())
                .password(passwordEncoder.encode(userDTO.getPassword()))
                .email(userDTO.getEmail())
                .name(userDTO.getName())
                .phone(userDTO.getPhone())
                .roles(userDTO.getRoles() != null ? Set.copyOf(userDTO.getRoles()) : Set.of("ROLE_USER"))
                .isEnabled(true)
                .isLocked(false)
                .build();

        userRepository.save(user);
        log.info("사용자가 등록되었습니다: {}", user.getUsername());
    }

    /**
     * 사용자 정보 수정
     */
    @Transactional
    public void updateUser(String id, UserDTO userDTO) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다: " + id));

        // 사용자명 변경 시 중복 확인
        if (!user.getUsername().equals(userDTO.getUsername()) 
                && userRepository.existsByUsername(userDTO.getUsername())) {
            throw new RuntimeException("이미 존재하는 사용자명입니다.");
        }

        // 이메일 변경 시 중복 확인
        if (userDTO.getEmail() != null 
                && !userDTO.getEmail().equals(user.getEmail())
                && userRepository.existsByEmail(userDTO.getEmail())) {
            throw new RuntimeException("이미 존재하는 이메일입니다.");
        }

        user.setUsername(userDTO.getUsername());
        user.setEmail(userDTO.getEmail());
        user.setName(userDTO.getName());
        user.setPhone(userDTO.getPhone());
        
        if (userDTO.getRoles() != null) {
            user.setRoles(Set.copyOf(userDTO.getRoles()));
        }

        // 비밀번호 변경 (새 비밀번호가 제공된 경우에만)
        if (userDTO.getPassword() != null && !userDTO.getPassword().trim().isEmpty()) {
            user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        }

        userRepository.save(user);
        log.info("사용자 정보가 수정되었습니다: {}", user.getUsername());
    }

    /**
     * 사용자 삭제
     */
    @Transactional
    public void deleteUser(String id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다: " + id));
        
        userRepository.delete(user);
        log.info("사용자가 삭제되었습니다: {}", user.getUsername());
    }

    /**
     * 사용자명 존재 여부 확인
     */
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    /**
     * 계정 활성화/비활성화 토글
     */
    @Transactional
    public void toggleEnabled(String id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다: " + id));
        
        user.setIsEnabled(!user.getIsEnabled());
        userRepository.save(user);
        log.info("계정 활성화 상태 변경: {} -> {}", user.getUsername(), user.getIsEnabled());
    }

    /**
     * 계정 잠금/해제 토글
     */
    @Transactional
    public void toggleLocked(String id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다: " + id));
        
        user.setIsLocked(!user.getIsLocked());
        if (!user.getIsLocked()) {
            user.setFailedLoginAttempts(0); // 잠금 해제 시 실패 횟수 초기화
        }
        userRepository.save(user);
        log.info("계정 잠금 상태 변경: {} -> {}", user.getUsername(), user.getIsLocked());
    }

    /**
     * 비밀번호 재설정
     */
    @Transactional
    public void resetPassword(String id, String newPassword) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다: " + id));
        
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        log.info("비밀번호가 재설정되었습니다: {}", user.getUsername());
    }

    /**
     * 로그인 성공 처리
     */
    @Transactional
    public void onLoginSuccess(String username) {
        userRepository.findByUsername(username).ifPresent(user -> {
            user.onLoginSuccess();
            userRepository.save(user);
        });
    }

    /**
     * 로그인 실패 처리
     */
    @Transactional
    public void onLoginFailure(String username) {
        userRepository.findByUsername(username).ifPresent(user -> {
            user.onLoginFailure();
            userRepository.save(user);
        });
    }

    /**
     * Entity -> DTO 변환
     */
    private UserDTO convertToDTO(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .name(user.getName())
                .phone(user.getPhone())
                .roles(user.getRoles() != null ? List.copyOf(user.getRoles()) : List.of())
                .isEnabled(user.getIsEnabled())
                .isLocked(user.getIsLocked())
                .lastLoginAt(user.getLastLoginAt())
                .failedLoginAttempts(user.getFailedLoginAttempts())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }
}
