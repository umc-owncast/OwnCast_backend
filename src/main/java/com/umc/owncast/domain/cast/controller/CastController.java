package com.umc.owncast.domain.cast.controller;

import com.umc.owncast.common.response.ApiResponse;
import com.umc.owncast.domain.cast.dto.CastCreationRequestDTO;
import com.umc.owncast.domain.cast.service.CastService;
import com.umc.owncast.domain.cast.service.ChatGPTScriptService;
import com.umc.owncast.domain.cast.service.ScriptService;
import com.umc.owncast.domain.cast.service.StreamService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.MalformedURLException;

@Tag(name = "캐스트 API", description = "캐스트 관련 API입니다")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cast")
public class CastController {
    private final CastService castService;
    private final ScriptService scriptService;
    private final StreamService streamService;

    /* ScriptService 테스트 */
    @PostMapping("/script")
    @Operation(summary = "스크립트 생성 API (ScriptService 테스트용)")
    public String createScript(@Valid @RequestBody CastCreationRequestDTO castRequest){
        System.out.println(castRequest);
        return scriptService.createScript(castRequest);
    }

    @PostMapping
    public ApiResponse<Object> createCast(@Valid @RequestBody CastCreationRequestDTO castRequest){
        return null;
    }
}