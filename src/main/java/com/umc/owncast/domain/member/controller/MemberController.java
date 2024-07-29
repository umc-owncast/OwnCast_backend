package com.umc.owncast.domain.member.controller;

import com.umc.owncast.common.jwt.JwtTokenDTO;
import com.umc.owncast.common.response.ApiResponse;
import com.umc.owncast.domain.member.dto.MemberLoginRequestDTO;
import com.umc.owncast.domain.member.dto.MemberResponseDTO;
import com.umc.owncast.domain.member.dto.MemberSignUpRequestDTO;
import com.umc.owncast.domain.member.service.MemberLoginServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/users")
@RestController
public class MemberController {

    private final MemberLoginServiceImpl memberLoginServiceImpl;

    @CrossOrigin
    @Operation(summary = "회원가입 API")
    @PostMapping("/signup")
    public ApiResponse<String> signUp(@Valid @RequestBody MemberSignUpRequestDTO memberSignUpRequestDto) {
        return null;
    }

    @CrossOrigin
    @Operation(summary = "로그인 API")
    @PostMapping("/login")
    public JwtTokenDTO signIn(@RequestBody MemberLoginRequestDTO signInDto) {
        String id = signInDto.getId();
        String password = signInDto.getPassword();
        JwtTokenDTO jwtToken = memberLoginServiceImpl.login(id, password);
        log.info("request id = {}, password = {}", id, password);
        log.info("jwtToken accessToken = {}, refreshToken = {}", jwtToken.getAccessToken(), jwtToken.getRefreshToken());
        return jwtToken;
    }

}
