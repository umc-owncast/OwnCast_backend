package com.umc.owncast.domain.member.controller;

import com.umc.owncast.common.jwt.LoginService;
import com.umc.owncast.common.jwt.dto.LoginDTO;
import com.umc.owncast.common.jwt.dto.TokenDTO;
import com.umc.owncast.common.response.ApiResponse;
import com.umc.owncast.common.response.status.SuccessCode;
import com.umc.owncast.domain.member.annotation.AuthUser;
import com.umc.owncast.domain.member.dto.*;
import com.umc.owncast.domain.member.dto.MemberRequest.JoinLoginIdDto;
import com.umc.owncast.domain.member.dto.MemberRequest.LoginDto;
import com.umc.owncast.domain.member.entity.Member;
import com.umc.owncast.domain.member.service.MemberMapper;
import com.umc.owncast.domain.member.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import static com.umc.owncast.common.response.status.SuccessCode._SIGNUP_SUCCESS;

@Tag(name = "유저 관련 API", description = "유저 관련 API입니다")
@RequiredArgsConstructor
@RequestMapping("/api/users")
@RestController
public class MemberController {

    private final MemberService memberService;
    private final LoginService loginService;

    @Operation(summary = "회원가입 API")
    @PostMapping("/signup")
    public ApiResponse<RefreshTokenDto> joinByLoginId(HttpServletResponse response,
                                                      @Valid @RequestBody JoinLoginIdDto requestDto) {
        return ApiResponse.of(_SIGNUP_SUCCESS, memberService.insertMember(response, requestDto));
    }

    @Operation(summary = "로그인 API")
    @PostMapping("/login")
    public ApiResponse<TokenDTO> login(@Valid @RequestBody LoginDto request) {
        //Filter에서 작동, swagger 틀만 작성
        LoginDTO loginDTO = new LoginDTO(request.getLoginId(), request.getPassword());
        return ApiResponse.onSuccess(loginService.login(loginDTO));
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
    public ApiResponse<RefreshTokenDto> reissue(HttpServletRequest request, HttpServletResponse response) {
        String newRefreshToken = memberService.reissueToken(request, response);
        return ApiResponse.of(SuccessCode._OK, MemberMapper.toRefreshToken(newRefreshToken));
    }

    @PostMapping("/logout")
    public ApiResponse<?> logout() {
        // Filter에서 작동하지만, Swagger 위해서 틀만 작성
        return ApiResponse.onSuccess(SuccessCode._OK);
    }

    @CrossOrigin
    @Operation(summary = "현재 로그인한 유저 정보 가져오기")
    @GetMapping("/info")
    public ApiResponse<MemberInfoDTO> getInfo(@AuthUser Member member) {
        return ApiResponse.onSuccess(memberService.getMemberInfo(member));
    }

    @CrossOrigin
    @Operation(summary = "언어 설정 바꾸기")
    @PostMapping("/setting/language")
    public ApiResponse<MemberSettingResponseDTO> language(@AuthUser Member member, @RequestBody LanguageDTO languageDTO) {
        return ApiResponse.onSuccess(memberService.languageSetting(member, languageDTO.getLanguage()));
    }

    @CrossOrigin
    @Operation(summary = "관심사 설정 바꾸기")
    @PostMapping("/setting/prefer")
    public ApiResponse<MemberSettingResponseDTO> category(@AuthUser Member member, @Valid @RequestBody MemberPreferRequestDTO memberPreferRequestDTO) {
        return ApiResponse.onSuccess(memberService.preferSetting(member, memberPreferRequestDTO));
    }

    @CrossOrigin
    @Operation(summary = "닉네임, 이름, 아이디 바꾸기")
    @PostMapping("/setting")
    public ApiResponse<MemberSettingResponseDTO> profileSetting(@AuthUser Member member, @Valid @RequestBody MemberProfileRequestDTO memberProfileRequestDTO) {
        return ApiResponse.onSuccess(memberService.profileSetting(member, memberProfileRequestDTO));
    }

    @CrossOrigin
    @Operation(summary = "비밀번호 바꾸기")
    @PostMapping("/setting/password")
    public ApiResponse<MemberSettingResponseDTO> passwordSetting(@AuthUser Member member, @Valid @RequestBody MemberPasswordRequestDTO memberPasswordRequestDTO) {
        return ApiResponse.onSuccess(memberService.passwordSetting(member, memberPasswordRequestDTO));
    }

}
