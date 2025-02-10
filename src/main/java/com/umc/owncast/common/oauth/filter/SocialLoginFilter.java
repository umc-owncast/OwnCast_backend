package com.umc.owncast.common.oauth.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.umc.owncast.domain.member.service.KakaoService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@Slf4j
public class SocialLoginFilter extends AbstractAuthenticationProcessingFilter {

    private static final String DEFAULT_LOGIN_REQUEST_URL = "/api/users/social-login";
    private static final String HTTP_METHOD = "POST";
    private static final String CONTENT_TYPE = "application/json";
    private static final String TOKEN_KEY = "token";
    private static final String PROVIDER_KEY = "provider";
    private static final String FCM_TOKEN_KEY = "fcmToken";
    private static final String KAKAO = "kakao";

    private final ObjectMapper objectMapper;
    private final KakaoService kakaoService;

    public SocialLoginFilter(ObjectMapper objectMapper, KakaoService kakaoService) {
        super(new AntPathRequestMatcher(DEFAULT_LOGIN_REQUEST_URL, HTTP_METHOD));
        this.objectMapper = objectMapper;
        this.kakaoService = kakaoService;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        if (request.getContentType() == null || !request.getContentType().startsWith(CONTENT_TYPE)) {
            throw new AuthenticationServiceException("Authentication Content-Type not supported: " + request.getContentType());
        }

        String messageBody = StreamUtils.copyToString(request.getInputStream(), StandardCharsets.UTF_8);
        Map<String, String> map = objectMapper.readValue(messageBody, Map.class);
        String token = map.get(TOKEN_KEY);
        String provider = map.get(PROVIDER_KEY);
        String fcmToken = map.get(FCM_TOKEN_KEY);

        String loginId;
        if (provider.equals(KAKAO)) {
            loginId = kakaoService.getMemberInfo(token, provider).getLoginId();
        } else {
            throw new NoSuchElementException("해당 소셜 로그인 종류는 존재하지 않습니다.");
        }

        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(loginId, null);

        Map<String, Object> additionalDetails = new HashMap<>();
        additionalDetails.put(FCM_TOKEN_KEY, fcmToken);
        additionalDetails.put(PROVIDER_KEY, provider);
        authRequest.setDetails(additionalDetails);

        return this.getAuthenticationManager().authenticate(authRequest);
    }
}
