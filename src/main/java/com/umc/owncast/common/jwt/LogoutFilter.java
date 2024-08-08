package com.umc.owncast.common.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.umc.owncast.common.exception.GeneralException;
import com.umc.owncast.common.response.ApiResponse;
import com.umc.owncast.common.response.status.ErrorCode;
import com.umc.owncast.common.response.status.SuccessCode;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class LogoutFilter extends OncePerRequestFilter {

    private final LoginService loginService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        if (!(request.getMethod().equals("POST") && request.getRequestURI().matches("/v1/meembers/logout"))) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            String refreshToken = loginService.validateRefreshToken(request.getCookies());
            loginService.revokeRefreshToken(refreshToken);

            writeOutput(request, response, HttpServletResponse.SC_OK, ApiResponse.onSuccess(SuccessCode._OK));
        } catch (Exception e) {
            writeOutput(request, response, HttpServletResponse.SC_BAD_REQUEST, ApiResponse.ofFailure(ErrorCode._BAD_REQUEST, null));
        }
    }

    private void writeOutput(HttpServletRequest request, HttpServletResponse response, int statusCode, ApiResponse<?> data) {
        try {
            response.setStatus(statusCode);
            response.setHeader("Content-Type", "application/json");
            response.getOutputStream().write(objectMapper.writeValueAsBytes(data));
        } catch (Exception e) {
            GeneralException newException = new GeneralException(ErrorCode.OUTPUT_ERROR);
            request.setAttribute("exception", newException);
            throw newException;
        }
    }
}
