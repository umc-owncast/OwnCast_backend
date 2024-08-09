package com.umc.owncast.domain.member.controller;

import com.umc.owncast.common.response.ApiResponse;
import com.umc.owncast.common.response.status.SuccessCode;
import com.umc.owncast.domain.member.dto.CustomUserDetails;
import com.umc.owncast.domain.member.dto.MemberRequest;
import com.umc.owncast.domain.member.dto.MemberResponse;
import com.umc.owncast.domain.member.entity.Member;
import com.umc.owncast.domain.member.service.MemberMapper;
import com.umc.owncast.domain.member.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/users")
@RestController
public class MemberController {


    private final MemberService memberService;

    @Operation(summary = "회원가입 API")
    @PostMapping("/signup")
    public ApiResponse<MemberResponse.refreshTokenDto> joinByLoginId(HttpServletResponse response,
                                                              @Valid @RequestBody MemberRequest.joinLoginIdDto requestDto) {
        String refreshToken = memberService.insertMember(response, requestDto);
        return ApiResponse.of(SuccessCode._SIGNUP_SUCCESS, MemberMapper.toRefreshToken(refreshToken));
    }

    @Operation(summary = "회원가입 언어 관심분야 API")
    @PostMapping("/signup/{memberId}")
    public ApiResponse<String> joinByCategory() {
        return null;
    }


    @Operation(summary = "로그인 API")
    @PostMapping("/login")
    public ApiResponse<?> login(@Valid @RequestBody MemberRequest.loginDto request) {
        //Filter에서 작동, swagger 틀만 작성
        return ApiResponse.onSuccess(SuccessCode._LOGIN_SUCCESS);
    }

    @Operation(summary = "토큰 재발급 api", description = "Cookie에 기존 refresh 토큰 필요, 헤더의 Authorization에 access 토큰, 바디(쿠키)에 refresh 토큰 반환")
    @PostMapping("/reissue")
    public ApiResponse<MemberResponse.refreshTokenDto> reissue(HttpServletRequest request, HttpServletResponse response) {
        String newRefreshToken = memberService.reissueToken(request, response);
        return ApiResponse.of(SuccessCode._OK, MemberMapper.toRefreshToken(newRefreshToken));
    }

    @PostMapping("/logout")
    public ApiResponse<?> logout() {
        // Filter에서 작동하지만, Swagger 위해서 틀만 작성
        return ApiResponse.onSuccess(SuccessCode._OK);
    }

    @Operation(summary = "회원 재활성 api")
    @PatchMapping("/reactivate")
    public ApiResponse<?> reactivate(@AuthenticationPrincipal CustomUserDetails authentication,
                                    @RequestBody @Valid MemberRequest.loginDto request) {
        memberService.reactivate(request);
        return ApiResponse.onSuccess(SuccessCode._OK);
    }

}
