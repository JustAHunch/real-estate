package com.hunch.realestate.repository;

import com.hunch.realestate.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 사용자 Repository
 */
@Repository
public interface UserRepository extends JpaRepository<User, String> {

    /**
     * 사용자명으로 조회
     */
    Optional<User> findByUsername(String username);

    /**
     * 이메일로 조회
     */
    Optional<User> findByEmail(String email);

    /**
     * 사용자명 존재 여부 확인
     */
    boolean existsByUsername(String username);

    /**
     * 이메일 존재 여부 확인
     */
    boolean existsByEmail(String email);

    /**
     * 활성화된 사용자 목록 조회
     */
    List<User> findByIsEnabled(Boolean isEnabled);

    /**
     * 잠긴 계정 목록 조회
     */
    List<User> findByIsLocked(Boolean isLocked);

    /**
     * 특정 권한을 가진 사용자 조회
     */
    @Query("SELECT u FROM User u JOIN u.roles r WHERE r = :role")
    List<User> findByRole(@Param("role") String role);

    /**
     * 사용자명 또는 이름으로 검색
     */
    @Query("SELECT u FROM User u WHERE u.username LIKE %:keyword% OR u.name LIKE %:keyword%")
    List<User> searchByUsernameOrName(@Param("keyword") String keyword);
}
