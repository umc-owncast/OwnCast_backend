package com.umc.owncast.common.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.umc.owncast.common.exception.GeneralException;
import com.umc.owncast.common.jwt.dto.LoginDTO;
import com.umc.owncast.common.jwt.dto.LoginResponseDTO;
import com.umc.owncast.common.response.ApiResponse;
import com.umc.owncast.common.response.status.ErrorCode;
import com.umc.owncast.common.response.status.SuccessCode;
import com.umc.owncast.domain.enums.Status;
import com.umc.owncast.domain.member.dto.CustomUserDetails;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final LoginService loginService;
    private final ObjectMapper objectMapper = new ObjectMapper();


    public LoginFilter(AuthenticationManagerBuilder authenticationManagerBuilder, LoginService loginService) {
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.loginService = loginService;

        setFilterProcessesUrl("/api/users/login");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        LoginDTO loginDTO;

        try {
            ServletInputStream inputStream = request.getInputStream();
            String messageBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
            loginDTO = objectMapper.readValue(messageBody, LoginDTO.class);
        } catch (IOException e) {
            writeOutput(request, response, HttpServletResponse.SC_BAD_REQUEST, ApiResponse.ofFailure(ErrorCode.INVALID_PARAMETER, null));
            return null;
        }

        String loginId = loginDTO.getLoginId();
        String password = loginDTO.getPassword();

         UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(loginId, password);

        return authenticationManagerBuilder.getObject().authenticate(authToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) {
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();

        if (customUserDetails.getStatus() == Status.INACTIVE) {
            writeOutput(request, response, HttpServletResponse.SC_FORBIDDEN, ApiResponse.onSuccess(ErrorCode.INACTIVATE_FORBIDDEN));
            return;
        }

        Long userId = customUserDetails.getUserId();

        String accessToken = loginService.issueAccessToken(userId);
        //Cookie refreshTokenCookie = loginService.issueRefreshToken(userId);
        String refreshToken = loginService.issueRefreshToken(userId);

        response.addHeader("Authorization", accessToken);
        //response.addCookie(refreshTokenCookie);
        writeOutput(request, response, HttpServletResponse.SC_OK, ApiResponse.of(SuccessCode._OK, new LoginResponseDTO(accessToken, refreshToken)));
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) {
        writeOutput(request, response, HttpServletResponse.SC_UNAUTHORIZED, ApiResponse.ofFailure(ErrorCode.LOGIN_FAILED, null));
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
