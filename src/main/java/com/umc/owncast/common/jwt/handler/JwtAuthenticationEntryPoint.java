package com.umc.owncast.common.jwt.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.umc.owncast.common.response.ApiResponse;
import com.umc.owncast.common.response.ErrorReasonDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

// 인증 실패 시
@Component
@RequiredArgsConstructor
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {

        ErrorReasonDTO error = ErrorReasonDTO.builder()
                .httpStatus(HttpStatus.UNAUTHORIZED)
                .isSuccess(false)
                .code("AUTH")
                .message("UNAUTHORIZED")
                .build();

        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getOutputStream().println(objectMapper.writeValueAsString(ApiResponse.onFailure(error.getCode(), error.getMessage(), null)));
    }
}
