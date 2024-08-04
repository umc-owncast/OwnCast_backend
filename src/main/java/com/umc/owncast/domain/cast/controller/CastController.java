package com.umc.owncast.domain.cast.controller;

import com.umc.owncast.domain.cast.dto.CastCreationRequestDTO;
import com.umc.owncast.domain.cast.service.CastService;
import com.umc.owncast.domain.cast.service.ScriptService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "캐스트 API", description = "캐스트 관련 API입니다")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cast")
public class CastController {
    private final CastService castService;
    private final ScriptService scriptService;

    /* ScriptService 테스트 */
    @PostMapping("/script")
    @Operation(summary = "스크립트 생성 API (ScriptService 테스트용)")
    public String createScript(@Valid @RequestBody CastCreationRequestDTO castRequest){
        System.out.println(castRequest);
        return scriptService.createScript(castRequest);
    }

    /*cast 저장 전 api*/
    @PostMapping("/temporary")
    @Operation(summary = "스크립트 생성 api. 저장 버튼 전 화면 입니다.")
    public String createCast(@Valid @RequestBody CastCreationRequestDTO castRequest){
        return castService.createCast(castRequest);
    }
}
