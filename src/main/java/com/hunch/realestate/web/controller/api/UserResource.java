package com.hunch.realestate.web.controller.api;

import com.hunch.realestate.domain.dto.UserDTO;
import com.hunch.realestate.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 사용자 관리 REST API 컨트롤러
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/admin/users")
@RequiredArgsConstructor
public class UserResource {

    private final UserService userService;

    /**
     * 사용자 목록 조회
     */
    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        try {
            List<UserDTO> users = userService.getAllUsers();
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            log.error("사용자 목록 조회 중 오류 발생: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * 사용자 상세 조회
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUser(@PathVariable String id) {
        try {
            UserDTO user = userService.getUserById(id);
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            log.error("사용자 조회 중 오류 발생: ", e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    /**
     * 사용자 등록
     */
    @PostMapping
    public ResponseEntity<?> registerUser(@RequestBody UserDTO userDTO) {
        // 유효성 검사
        Map<String, String> errors = userDTO.validate();
        if (!errors.isEmpty()) {
            return ResponseEntity.badRequest().body(errors);
        }

        try {
            userService.registerUser(userDTO);
            return ResponseEntity.ok().body("사용자가 등록되었습니다.");
        } catch (Exception e) {
            log.error("사용자 등록 중 오류 발생: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("사용자 등록 중 오류가 발생했습니다: " + e.getMessage());
        }
    }

    /**
     * 사용자 수정
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(
            @PathVariable String id,
            @RequestBody UserDTO userDTO) {

        // 유효성 검사
        Map<String, String> errors = userDTO.validate();
        if (!errors.isEmpty()) {
            return ResponseEntity.badRequest().body(errors);
        }

        try {
            userService.updateUser(id, userDTO);
            return ResponseEntity.ok().body("사용자 정보가 수정되었습니다.");
        } catch (Exception e) {
            log.error("사용자 수정 중 오류 발생: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("사용자 수정 중 오류가 발생했습니다: " + e.getMessage());
        }
    }

    /**
     * 사용자 삭제
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable String id) {
        try {
            userService.deleteUser(id);
            return ResponseEntity.ok().body("사용자가 삭제되었습니다.");
        } catch (Exception e) {
            log.error("사용자 삭제 중 오류 발생: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("사용자 삭제 중 오류가 발생했습니다: " + e.getMessage());
        }
    }

    /**
     * 사용자명 중복 확인
     */
    @GetMapping("/check-username")
    public ResponseEntity<Map<String, Boolean>> checkUsername(@RequestParam String username) {
        try {
            boolean exists = userService.existsByUsername(username);
            return ResponseEntity.ok(Map.of("exists", exists));
        } catch (Exception e) {
            log.error("사용자명 확인 중 오류 발생: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * 계정 활성화/비활성화
     */
    @PatchMapping("/{id}/toggle-enabled")
    public ResponseEntity<?> toggleEnabled(@PathVariable String id) {
        try {
            userService.toggleEnabled(id);
            return ResponseEntity.ok().body("계정 상태가 변경되었습니다.");
        } catch (Exception e) {
            log.error("계정 상태 변경 중 오류 발생: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("계정 상태 변경 중 오류가 발생했습니다.");
        }
    }

    /**
     * 계정 잠금/해제
     */
    @PatchMapping("/{id}/toggle-locked")
    public ResponseEntity<?> toggleLocked(@PathVariable String id) {
        try {
            userService.toggleLocked(id);
            return ResponseEntity.ok().body("계정 잠금 상태가 변경되었습니다.");
        } catch (Exception e) {
            log.error("계정 잠금 상태 변경 중 오류 발생: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("계정 잠금 상태 변경 중 오류가 발생했습니다.");
        }
    }

    /**
     * 비밀번호 재설정
     */
    @PatchMapping("/{id}/reset-password")
    public ResponseEntity<?> resetPassword(
            @PathVariable String id,
            @RequestBody Map<String, String> request) {
        
        String newPassword = request.get("newPassword");
        if (newPassword == null || newPassword.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("새 비밀번호를 입력해주세요.");
        }

        try {
            userService.resetPassword(id, newPassword);
            return ResponseEntity.ok().body("비밀번호가 재설정되었습니다.");
        } catch (Exception e) {
            log.error("비밀번호 재설정 중 오류 발생: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("비밀번호 재설정 중 오류가 발생했습니다.");
        }
    }
}
