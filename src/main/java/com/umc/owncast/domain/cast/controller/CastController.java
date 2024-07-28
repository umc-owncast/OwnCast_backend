package com.umc.owncast.domain.cast.controller;

import com.umc.owncast.domain.cast.dto.CastCreationRequestDTO;
import com.umc.owncast.domain.cast.service.CastService;
import com.umc.owncast.domain.cast.service.ScriptService;
import com.umc.owncast.domain.cast.service.StreamService;
import com.umc.owncast.domain.cast.service.TTSService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@Tag(name = "캐스트 API", description = "캐스트 관련 API입니다")
@CrossOrigin
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

    /*cast 저장 전 api*/
    @PostMapping("temporary")
    @Operation(summary = "스크립트 생성 api. 저장 버튼 전 화면 입니다.")
    public String createCast(@Valid @RequestBody CastCreationRequestDTO castRequest){
        return castService.createCast(castRequest);
    }


    @GetMapping("stream-test")
    @Operation(summary = "스트리밍 테스트. 테스트용 음악 파일을 스트리밍 합니다")
    public Object streamTest(@RequestHeader HttpHeaders headers) throws IOException {
        return streamService.stream("LastCarnival.mp3", headers);
    }
}
