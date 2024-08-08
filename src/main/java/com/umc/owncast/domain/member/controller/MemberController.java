package com.umc.owncast.domain.member.controller;

import com.umc.owncast.common.response.ApiResponse;
import com.umc.owncast.domain.member.dto.*;
import com.umc.owncast.domain.member.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/users")
@RestController
public class MemberController {

    private final UserService userService;

    @CrossOrigin
    @Operation(summary = "회원가입 API")
    @PostMapping("/signup")
    public ApiResponse<String> signUp(@Valid @RequestBody MemberSignUpRequestDTO memberSignUpRequestDto) {
        return null;
    }

    @CrossOrigin
    @Operation(summary = "로그인 API")
    @PostMapping("/login")
    public ApiResponse<MemberResponseDTO> login(@Valid @RequestBody MemberLoginRequestDTO memberLoginRequestDTO) {
        return null;
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
    public ApiResponse<Long> language(@RequestBody MemberProfileRequestDTO memberProfileRequestDTO) {
        //
        return ApiResponse.onSuccess(123L);
    }
}
