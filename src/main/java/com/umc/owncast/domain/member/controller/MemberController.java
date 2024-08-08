package com.umc.owncast.domain.member.controller;

import com.umc.owncast.common.response.ApiResponse;
import com.umc.owncast.common.response.status.SuccessCode;
import com.umc.owncast.domain.member.dto.MemberRequest;
import com.umc.owncast.domain.member.dto.MemberResponse;
import com.umc.owncast.domain.member.entity.Member;
import com.umc.owncast.domain.member.service.MemberMapper;
import com.umc.owncast.domain.member.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/users")
@RestController
public class MemberController {


    private final MemberService memberService;

    @Operation(summary = "회원가입 API")
    @PostMapping("/signup")
    public ApiResponse<MemberResponse.refreshTokenDto> signup(HttpServletResponse response,
                                                              @Valid @RequestBody MemberRequest.joinLoginIdDto requestDto) {
        String refreshToken = memberService.insertMember(response, requestDto);
        return ApiResponse.of(SuccessCode._SIGNUP_SUCCESS, MemberMapper.toRefreshToken(refreshToken));
    }

    @Operation(summary = "회원가입 언어 관심분야 API")
    @PostMapping("/signup/{memberId}")
    public ApiResponse<String> signUp() {
        return null;
    }


    @Operation(summary = "로그인 API")
    @PostMapping("/login")
    public ApiResponse<?> login(@Valid @RequestBody MemberRequest.loginDto request) {
        return ApiResponse.onSuccess(SuccessCode._LOGIN_SUCCESS);
    }
}
