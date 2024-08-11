package com.umc.owncast.domain.cast.controller;

import com.umc.owncast.common.response.ApiResponse;
import com.umc.owncast.domain.cast.dto.CastSaveDTO;
import com.umc.owncast.domain.cast.dto.CastUpdateDTO;
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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.umc.owncast.domain.cast.service.KeywordService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "캐스트 API", description = "캐스트 관련 API입니다")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cast")
public class CastController {
    private final KeywordService keywordService;
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

    /*cast 저장 전 api
    @PostMapping("/temporary")
    @Operation(summary = "스크립트 생성 api. 저장 버튼 전 화면 입니다.")
    public void createCast(@Valid @RequestBody KeywordCastCreationDTO castRequest){
        castService.createCast(castRequest);
    }*/

    /*@GetMapping("/stream-test")
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
    }*/


    /* * * * * * * *
    *  API 용 메소드 *
    * * * * * * * **/

    /* Cast 생성 API (keyword) */
    @PostMapping("/keyword")
    @Operation(summary = "키워드로 캐스트를 생성하는 API")
    public ApiResponse<Object> createCastByKeyword(@Valid @RequestBody KeywordCastCreationDTO castRequest){
        return castService.createCastByKeyword(castRequest);
    }

    /* Cast 생성 API (script) */
    @PostMapping("/script")
    @Operation(summary = "스크립트로 캐스트를 생성하는 API.")
    public ApiResponse<Object> createCastByScript(@Valid @RequestBody ScriptCastCreationDTO castRequest){
        return castService.createCastByScript(castRequest);
    }

    /* Cast 저장 API */
    @PostMapping(value = "/{castId}", consumes = {MediaType.APPLICATION_JSON_VALUE , MediaType.MULTIPART_FORM_DATA_VALUE})
    @Operation(summary = "캐스트 저장 API (저장 화면에서 호출)")
    public ApiResponse<Object> saveCast(@PathVariable("castId") Long castId,
                                        @Valid @RequestPart(value = "saveInfo") CastSaveDTO saveRequest,
                                        @RequestPart(value = "image", required = false) MultipartFile image){
        System.out.println("CastController: save()");
        System.out.println(saveRequest);
        System.out.println(image);
        return castService.saveCast(castId, saveRequest, image);
    }

    /* Cast 재생 API */
    @GetMapping("/{castId}/audio")
    @Operation(summary = "캐스트 재생 API")
    @CrossOrigin
    public ResponseEntity<UrlResource> streamCast(@PathVariable("castId") Long castId,
                                                  @RequestHeader HttpHeaders headers){
        return castService.streamCast(castId, headers);
    }

    /* Cast 스크립트 가져오는 API */
    @GetMapping("/{castId}/scripts")
    @Operation(summary = "캐스트 스크립트 가져오기 API")
    public ApiResponse<Object> fetchCastScripts(@PathVariable("castId") Long castId){
        return castService.fetchCastScript(castId);
    }

    /* Cast 수정 API */
    @PatchMapping("/{castId}")
    @Operation(summary = "캐스트 수정 API")
    public ApiResponse<Object> updateCast(@PathVariable("castId") Long castId,
                                          @Valid @RequestPart(value = "updateInfo") CastUpdateDTO updateRequest,
                                          @RequestPart(value = "image", required = false) MultipartFile image){
        // TODO 캐스트 생성자 혹은 관리자여야 함
        return castService.updateCast(castId, updateRequest, image);
    }

    /* Cast 삭제 API */
    @DeleteMapping("/{castId}")
    @Operation(summary = "캐스트 삭제 API")
    public ApiResponse<Object> deleteCast(@PathVariable("castId") Long castId){
        // TODO 캐스트 생성자 혹은 관리자여야 함
        return castService.deleteCast(castId);
    }

    @GetMapping("/home")
    @Operation(summary = "홈 화면 키워드 6개 받아오기")
    public List<String> createScript() {
        return keywordService.createKeyword();
    }
}
