package com.umc.owncast.domain.cast.controller;

import com.umc.owncast.domain.cast.dto.CastCreationRequestDTO;
import com.umc.owncast.domain.cast.service.CastService;
import com.umc.owncast.domain.cast.service.ScriptService;
import com.umc.owncast.domain.cast.service.TTSService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "캐스트 API", description = "캐스트 관련 API입니다")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cast")
public class CastController {
    private final CastService castService;
    private final ScriptService scriptService;
    //private final StreamService streamService;

    /* ScriptService 테스트 */
    @PostMapping("/script")
    @Operation(summary = "스크립트 생성 API (ScriptService 테스트용)")
    public String createScript(@Valid @RequestBody CastCreationRequestDTO castRequest){
        System.out.println(castRequest);
        return scriptService.createScript(castRequest);
    }

    /* TTS Service 테스트*//*
    @PostMapping("/tts")
    @Operation(summary = "tts & timepoint 생성 test api")
    public String ttsTest(@Valid @RequestBody TTSDTO ttsdto) {
        return ttsService.createSpeech(ttsdto);
    }*/

    /*cast 저장 전 api*/
    @PostMapping("temporary")
    @Operation(summary = "스크립트 생성 api. 저장 버튼 전 화면 입니다.")
    public String createCast(@Valid @RequestBody CastCreationRequestDTO castRequest){
        return castService.createCast(castRequest);
    }
}
