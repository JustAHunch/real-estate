package com.hunch.realestate.config;

import com.hunch.realestate.domain.dto.UserDTO;
import com.hunch.realestate.service.JsonStorageService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.RequiredArgsConstructor;
import org.springframework.security.web.SecurityFilterChain;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JsonStorageService jsonStorageService;
    private final StorageProperties storageProperties;

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            List<UserDTO> users = jsonStorageService.readList(
                    storageProperties.getJson().getUsers(),
                    new TypeReference<List<UserDTO>>() {}
            );

            return users.stream()
                    .filter(user -> user.getUsername().equals(username))
                    .findFirst()
                    .map(user -> User.builder()
                            .username(user.getUsername())
                            .password("{noop}" + user.getPassword())
                            .roles(user.getRoles().toArray(String[]::new))
                            .build())
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        };
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        // 정적 리소스 및 로그인 관련 페이지 허용
                        .requestMatchers(
                                "/static/**",
                                "/css/**",
                                "/js/**",
                                "/images/**",
                                "/login",
                                "/error"
                        ).permitAll()
                        // 관리자 전용 페이지
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/api/v1/admin/**").hasRole("ADMIN")
                        // 나머지 모든 요청은 인증 필요
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/login")  // 로그인 처리 URL
                        .defaultSuccessUrl("/dashboard")  // 로그인 성공 후 리다이렉트
                        .failureUrl("/login?error=true")  // 로그인 실패 시 리다이렉트
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")  // 로그아웃 URL
                        .logoutSuccessUrl("/login?logout=true")  // 로그아웃 성공 시 리다이렉트
                        .invalidateHttpSession(true)  // 세션 무효화
                        .deleteCookies("JSESSIONID")  // 쿠키 삭제
                        .permitAll()
                );

        return http.build();
    }
}