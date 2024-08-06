package com.umc.owncast.domain.cast.controller;

import com.umc.owncast.domain.cast.dto.CastCreationRequestDTO;
import com.umc.owncast.domain.cast.service.CastService;
import com.umc.owncast.domain.cast.service.ScriptService;
import com.umc.owncast.domain.cast.service.StreamService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

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

    /*cast 저장 전 api*/
    @PostMapping("/temporary")
    @Operation(summary = "스크립트 생성 api. 저장 버튼 전 화면 입니다.")
    public String createCast(@Valid @RequestBody CastCreationRequestDTO castRequest){
        return castService.createCast(castRequest);
    }


    @GetMapping("stream-test")
    @CrossOrigin(origins = "*") // TODO 프론트 url로 대체
    @Operation(summary = "스트리밍 테스트. 테스트용 음악 파일을 스트리밍 합니다")
    public Object streamTest(@RequestHeader HttpHeaders headers) throws IOException {
        System.out.println("Stream test");
        return streamService.stream("test.mp3", headers);
    }

    @GetMapping("/stream/{filename}")
    @CrossOrigin(origins = "*")
    @Operation(summary = "filename을 스트리밍합니다")
    public Object streamTest(@RequestHeader HttpHeaders headers,
                             @PathVariable(name = "filename") String filename) throws IOException {
        return streamService.stream(filename, headers);
    }
}
