package com.umc.owncast.domain.voiceexample.controller;

import com.umc.owncast.common.response.ApiResponse;
import com.umc.owncast.common.response.status.SuccessCode;
import com.umc.owncast.domain.member.annotation.AuthUser;
import com.umc.owncast.domain.member.entity.Member;
import com.umc.owncast.domain.voiceexample.dto.VoiceExampleDTO;
import com.umc.owncast.domain.voiceexample.service.VoiceExampleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Tag(name = "보이스 API", description = "보이스 관련 API입니다")
@Controller
@RequiredArgsConstructor
public class VoiceExampleController {
    private final VoiceExampleService voiceExampleService;

    @GetMapping("/api/voice/examples")
    @Operation(summary = "회원에 알맞는 목소리 오디오 경로 반환하는 API")
    public ApiResponse<List<VoiceExampleDTO>> fetchVoiceExamples(@AuthUser Member member){
        return ApiResponse.of(SuccessCode._OK, voiceExampleService.fetchVoiceExamplesForMember(member));
    }

    @GetMapping("/api/voice/{voiceCode}/example")
    @Operation(summary = "특정 목소리 오디오 경로 반환하는 API")
    public ApiResponse<VoiceExampleDTO> fetchVoiceExample(@PathVariable("voiceCode") String voiceCode,
                                                          @AuthUser Member member){
        return ApiResponse.of(SuccessCode._OK, voiceExampleService.fetchVoiceExample(voiceCode, member));
    }
}
