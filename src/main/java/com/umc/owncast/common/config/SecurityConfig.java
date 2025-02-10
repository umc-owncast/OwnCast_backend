package com.umc.owncast.common.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.umc.owncast.common.jwt.*;
import com.umc.owncast.common.jwt.handler.JwtAccessDeniedHandler;
import com.umc.owncast.common.jwt.handler.JwtAuthenticationEntryPoint;
import com.umc.owncast.common.oauth.filter.SocialLoginFilter;
import com.umc.owncast.common.oauth.util.SocialProvider;
import com.umc.owncast.domain.member.service.CustomUserDetailsService;
import com.umc.owncast.domain.member.service.KakaoService;
import com.umc.owncast.domain.member.service.SocialLoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;


@Configuration
@EnableWebSecurity //기본적인 웹보안 활성화
@EnableMethodSecurity
@RequiredArgsConstructor
public class  SecurityConfig {

    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final SocialLoginService socialLoginService;
    private final CustomUserDetailsService customUserDetailsService;
    private final ObjectMapper objectMapper;
    private final AuthenticationConfiguration authenticationConfiguration;
    private final LoginService loginService;
    private final KakaoService kakaoService;


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .addFilterBefore(jwtAuthenticationFilter, LoginFilter.class)
                .addFilterAt(new LoginFilter(authenticationManager(), loginService), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new LogoutFilter(loginService), org.springframework.security.web.authentication.logout.LogoutFilter.class)
                .addFilterBefore(socialLoginFilter(), JwtAuthenticationFilter.class) // SocialLoginFilter 추가
                .csrf(AbstractHttpConfigurer::disable)
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                        .accessDeniedHandler(jwtAccessDeniedHandler))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(registry -> registry
                        .requestMatchers("**").permitAll() // TODO 일시적으로 허용해놓음 -> 배포할 때 삭제
                        .requestMatchers("/favicon.ico", "/error", "/swagger-ui/**", "/swagger-ui.html/**", "/v3/api-docs/**",
                                "/api/users/signup", "/api/users/signup/**","api/users/login","api/users/check/**").permitAll()
                )
                .authorizeHttpRequests(registry -> registry
                        .anyRequest().authenticated());

        return http.build();
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
    public AuthenticationManager authenticationManager() {

        SocialProvider socialProvider = new SocialProvider(socialLoginService);

        DaoAuthenticationProvider loginProvider = new DaoAuthenticationProvider();
        loginProvider.setPasswordEncoder(passwordEncoder());
        loginProvider.setUserDetailsService(customUserDetailsService);

        return new ProviderManager(socialProvider, loginProvider);
    }

    @Bean
    public SocialLoginFilter socialLoginFilter() {
        SocialLoginFilter socialLoginFilter = new SocialLoginFilter(objectMapper, kakaoService);
        socialLoginFilter.setAuthenticationManager(authenticationManager());
        return socialLoginFilter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
