package com.hunch.realestate.config;

import com.hunch.realestate.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.util.stream.Collectors;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final UserRepository userRepository;

    /**
     * PasswordEncoder Bean 등록
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * UserDetailsService Bean 등록
     */
    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            com.hunch.realestate.domain.entity.User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다: " + username));

            // 계정 잠금 확인
            if (user.getIsLocked()) {
                throw new RuntimeException("계정이 잠겨있습니다.");
            }

            // 계정 활성화 확인
            if (!user.getIsEnabled()) {
                throw new RuntimeException("비활성화된 계정입니다.");
            }

            return User.builder()
                    .username(user.getUsername())
                    .password(user.getPassword())
                    .authorities(user.getRoles().stream()
                            .map(SimpleGrantedAuthority::new)
                            .collect(Collectors.toList()))
                    .accountExpired(false)
                    .accountLocked(user.getIsLocked())
                    .credentialsExpired(false)
                    .disabled(!user.getIsEnabled())
                    .build();
        };
    }

    /**
     * Security Filter Chain 설정
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        // H2 Console 접근 허용 (개발 환경)
                        .requestMatchers("/h2-console/**").permitAll()
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
                        .requestMatchers("/admin/**").hasAnyRole("ADMIN", "USER")
                        .requestMatchers("/api/v1/admin/**").hasAnyRole("ADMIN", "USER")
                        // 나머지 모든 요청은 인증 필요
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        .defaultSuccessUrl("/admin/properties/list", true)
                        .failureUrl("/login?error=true")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout=true")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                )
                // H2 Console을 위한 Frame 옵션 설정
                .headers(headers -> headers
                        .frameOptions(frame -> frame.sameOrigin())
                );

        return http.build();
    }
}
