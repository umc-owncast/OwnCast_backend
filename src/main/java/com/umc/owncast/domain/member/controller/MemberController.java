package com.umc.owncast.domain.member.controller;

import com.umc.owncast.common.response.ApiResponse;
import com.umc.owncast.common.response.status.SuccessCode;
import com.umc.owncast.domain.member.dto.*;
import com.umc.owncast.domain.member.service.MemberMapper;
import com.umc.owncast.domain.member.service.MemberService;
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

    @Operation(summary = "닉네임 중복 체크 API")
    @PostMapping("/check/member-nickname")
    public ApiResponse<Boolean> nickNameDuplicate(@RequestParam("nickName") String nickName) {
        return ApiResponse.onSuccess(memberService.nickNameDuplicate(nickName));
    }

    @Operation(summary = "Id 중복체크 API")
    @PostMapping("/check/login-id")
    public ApiResponse<Boolean> memberIdDuplicate(@RequestParam("loginId") String loginId) {
        return ApiResponse.onSuccess(memberService.memberIdDuplicate(loginId));
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
        return ApiResponse.onSuccess(memberService.languageSetting(languageId));
    }

    @CrossOrigin
    @Operation(summary = "관심사 설정 바꾸기")
    @PostMapping("/setting/prefer")
    public ApiResponse<Long> category(@Valid @RequestBody MemberPreferRequestDTO memberPreferRequestDTO) {
        return ApiResponse.onSuccess(memberService.preferSetting(memberPreferRequestDTO));
    }

    @CrossOrigin
    @Operation(summary = "닉네임, 이름, 아이디 바꾸기")
    @PostMapping("/setting")
    public ApiResponse<Long> profileSetting(@Valid @RequestBody MemberProfileRequestDTO memberProfileRequestDTO) {
        return ApiResponse.onSuccess(memberService.profileSetting(memberProfileRequestDTO));
    }

    @CrossOrigin
    @Operation(summary = "비밀번호 바꾸기")
    @PostMapping("/setting/password")
    public ApiResponse<Long> passwordSetting(@Valid @RequestBody MemberPasswordRequestDTO memberPasswordRequestDTO) {
        return ApiResponse.onSuccess(memberService.passwordSetting(memberPasswordRequestDTO));
    }
}
