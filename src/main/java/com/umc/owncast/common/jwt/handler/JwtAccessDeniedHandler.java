package com.umc.owncast.common.jwt.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.umc.owncast.common.response.ApiResponse;
import com.umc.owncast.common.response.ErrorReasonDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

// 인가 실패
@Component
@RequiredArgsConstructor
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper objectMapper;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException e) throws IOException {
        ErrorReasonDTO error = ErrorReasonDTO.builder()
                .httpStatus(HttpStatus.FORBIDDEN)
                .isSuccess(false)
                .code("AUTH")
                .message("FORBIDDEN")
                .build();

        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json");
        response.getOutputStream().println(objectMapper.writeValueAsString(ApiResponse.onFailure(error.getCode(), error.getMessage(), null)));
    }
}
