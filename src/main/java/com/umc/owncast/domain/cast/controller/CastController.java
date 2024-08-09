package com.umc.owncast.domain.cast.controller;

import com.umc.owncast.common.response.ApiResponse;
import com.umc.owncast.domain.cast.dto.CastSaveDTO;
import com.umc.owncast.domain.cast.dto.KeywordCastCreationDTO;
import com.umc.owncast.domain.cast.dto.ScriptCastCreationDTO;
import com.umc.owncast.domain.cast.service.CastService;
import com.umc.owncast.domain.cast.service.ScriptService;
import com.umc.owncast.domain.cast.service.StreamService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
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

    /* * * * * * * * * * * * * *
    * 테스트용 메소드 (나중에 삭제) *
    * * * * * * * * * * * * * **/

    @PostMapping("/script-test")
    @Operation(summary = "스크립트 생성 API (ScriptService 테스트용)")
    public String createScript(@Valid @RequestBody KeywordCastCreationDTO castRequest){
        System.out.println(castRequest);
        return scriptService.createScript(castRequest);
    }

    /*cast 저장 전 api*/
    @PostMapping("/temporary")
    @Operation(summary = "스크립트 생성 api. 저장 버튼 전 화면 입니다.")
    public void createCast(@Valid @RequestBody KeywordCastCreationDTO castRequest){
        castService.createCast(castRequest);
    }

    @GetMapping("/stream-test")
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


    /* * * * * * * *
    *  API 용 메소드 *
    * * * * * * * **/

    /* Cast 생성 API (keyword) */
    @PostMapping("/keyword")
    @Operation(summary = "키워드로 캐스트를 생성하는 API")
    public ApiResponse<Object> createCastByKeyword(@Valid @RequestBody KeywordCastCreationDTO castRequest){
        return castService.createCast(castRequest);
    }

    /* Cast 생성 API (script) */
    @PostMapping("/script")
    @Operation(summary = "스크립트로 캐스트를 생성하는 API.")
    public ApiResponse<Object> createCastByScript(@Valid @RequestBody ScriptCastCreationDTO castRequest){
        return castService.createCast(castRequest);
    }

    /* Cast 저장 API */
    @PostMapping("/{castId}")
    @Operation(summary = "캐스트 저장 API (저장 화면에서 호출)")
    public ApiResponse<Object> saveCast(@PathVariable("castId") Long castId,
                                        @Valid @RequestBody CastSaveDTO saveRequest){
        return castService.saveCast(castId, saveRequest);
    }

    /* Cast 재생 API */
    @GetMapping("/{castId}")
    @Operation(summary = "캐스트 재생 API")
    public ResponseEntity<UrlResource> streamCast(@PathVariable("castId") Long castId,
                                                  @RequestHeader HttpHeaders headers){
        return castService.streamCast(castId, headers);
    }

    /* Cast 수정 API */

    /* Cast 삭제 API */

}
