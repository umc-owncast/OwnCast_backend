package com.umc.owncast.domain.member.controller;

import com.umc.owncast.common.response.ApiResponse;
import com.umc.owncast.domain.member.dto.MemberLoginRequestDTO;
import com.umc.owncast.domain.member.dto.MemberResponseDTO;
import com.umc.owncast.domain.member.dto.MemberSignUpRequestDTO;
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

}
