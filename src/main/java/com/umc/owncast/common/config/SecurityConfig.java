package com.umc.owncast.common.config;

import com.umc.owncast.common.jwt.*;
import com.umc.owncast.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;


@Configuration
@EnableWebSecurity //기본적인 웹보안 활성화
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtUtil jwtUtil;
    private final AuthenticationEntryPoint entryPoint;
    private final LoginService loginService;
    private final MemberRepository memberRepository;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity, AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        return httpSecurity
                .httpBasic(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .exceptionHandling(handler -> handler
                        .authenticationEntryPoint(entryPoint))
                .sessionManagement(sessionManagement -> sessionManagement
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(registry -> registry
                        .requestMatchers("**").permitAll() // TODO 일시적으로 허용해놓음 -> 배포할 때 삭제
                        .requestMatchers("/favicon.ico", "/error", "/swagger-ui/**", "/swagger-ui.html/**", "/v3/api-docs/**",
                                "/api/users/signup", "/api/users/signup/**","api/users/login","api/users/check/**").permitAll()
                )
                .addFilterBefore(new JwtFilter(jwtUtil, memberRepository), LoginFilter.class)
                .addFilterAt(new LoginFilter(authenticationManagerBuilder, loginService), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new LogoutFilter(loginService), org.springframework.security.web.authentication.logout.LogoutFilter.class)
                .build();
    }

    CorsConfigurationSource apiConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("*")); // 이후 수정
        configuration.setAllowedMethods(Arrays.asList("GET", "POST"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
