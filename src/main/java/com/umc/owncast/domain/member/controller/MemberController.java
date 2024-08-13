package com.umc.owncast.domain.member.controller;

import com.umc.owncast.common.response.ApiResponse;
import com.umc.owncast.common.response.status.SuccessCode;
import com.umc.owncast.domain.member.dto.MemberPreferRequestDTO;
import com.umc.owncast.domain.member.dto.MemberProfileRequestDTO;
import com.umc.owncast.domain.member.dto.MemberRequest;
import com.umc.owncast.domain.member.dto.MemberResponse;
import com.umc.owncast.domain.member.service.MemberMapper;
import com.umc.owncast.domain.member.service.MemberService;
import com.umc.owncast.domain.member.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


@Tag(name = "유저 관련 API", description = "유저 관련 API입니다")
@RequiredArgsConstructor
@RequestMapping("/api/users")
@RestController
public class MemberController {

    private final UserService userService;
    private final MemberService memberService;

    @Operation(summary = "회원가입 API")
    @PostMapping("/signup")
    public ApiResponse<MemberResponse.refreshTokenDto> joinByLoginId(HttpServletResponse response,
                                                                     @Valid @RequestBody MemberRequest.joinLoginIdDto requestDto) {
        String refreshToken = memberService.insertMember(response, requestDto);
        return ApiResponse.of(SuccessCode._SIGNUP_SUCCESS, MemberMapper.toRefreshToken(refreshToken));
    }

    @Operation(summary = "회원가입 언어 관심분야 API")
    @PostMapping("/signup/{member_id}")
    public ApiResponse<Long> joinSetting(@PathVariable("member_id") Long memberId,
                                         @RequestParam Long languageId,
                                         @Valid @RequestBody MemberRequest.memberPreferDto request) {

        Long updatedMemberId = memberService.addPreferSetting(memberId, request);

        return ApiResponse.of(SuccessCode._OK, memberService.addLanguage(languageId));
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


    @CrossOrigin
    @Operation(summary = "언어 설정 바꾸기")
    @PostMapping("/setting/language")
    public ApiResponse<Long> language(@RequestParam("languageId") Long languageId) {
        return ApiResponse.onSuccess(userService.languageSetting(languageId));
    }

    @CrossOrigin
    @Operation(summary = "관심사 설정 바꾸기")
    @PostMapping("/setting/prefer")
    public ApiResponse<Long> category(@Valid @RequestBody MemberPreferRequestDTO memberPreferRequestDTO) {
        return ApiResponse.onSuccess(userService.preferSetting(memberPreferRequestDTO));
    }

    @CrossOrigin
    @Operation(summary = "닉네임, 이름, 아이디 바꾸기")
    @PostMapping("/setting")
    public ApiResponse<Long> language(@Valid @RequestBody MemberProfileRequestDTO memberProfileRequestDTO) {
        //
        return ApiResponse.onSuccess(userService.idPasswordSetting(memberProfileRequestDTO));
    }
}
