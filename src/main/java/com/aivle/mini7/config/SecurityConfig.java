package com.aivle.mini7.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.http.SessionCreationPolicy;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // CSRF 비활성화
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/", "/login/**").permitAll() // 공개 경로 허용
                        .anyRequest().permitAll() // 나머지 요청도 허용
                )
                .formLogin(form -> form.disable()) // 기본 폼 로그인 비활성화
                .logout(logout -> logout
                        .logoutUrl("/logout") // 로그아웃 URL 설정
                        .logoutSuccessUrl("/") // 로그아웃 성공 후 리다이렉트
                        .invalidateHttpSession(true) // 세션 무효화
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.ALWAYS) // 세션 생성 정책: 항상 생성
                )
                .httpBasic(httpBasic -> httpBasic.disable()); // HTTP Basic 인증 비활성화
        return http.build();
    }
}
