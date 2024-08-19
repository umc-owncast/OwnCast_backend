package com.umc.owncast.domain.cast.controller;

import com.umc.owncast.common.response.ApiResponse;
import com.umc.owncast.common.response.status.SuccessCode;
import com.umc.owncast.domain.cast.dto.*;
import com.umc.owncast.domain.cast.entity.Cast;
import com.umc.owncast.domain.cast.service.*;
import com.umc.owncast.domain.cast.service.chatGPT.keyword.KeywordService;
import com.umc.owncast.domain.member.annotation.AuthUser;
import com.umc.owncast.domain.member.entity.Member;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Tag(name = "캐스트 API", description = "캐스트 관련 API입니다")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cast")
public class CastController {
    private final KeywordService keywordService;
    private final CastService castService;

    /* * * * * * * *
     *  API 용 메소드 *
     * * * * * * * **/

    /* Cast 생성 API (keyword) */
    @PostMapping("/keyword")
    @Operation(summary = "키워드로 캐스트를 생성하는 API")
    public ApiResponse<Object> createCastByKeyword(@Valid @RequestBody KeywordCastCreationDTO castRequest) {
        return ApiResponse.of(SuccessCode._OK, castService.createCastByKeyword(castRequest));
    }

    /* Cast 생성 API (script) */
    @PostMapping("/script")
    @Operation(summary = "스크립트로 캐스트를 생성하는 API.")
    public ApiResponse<Object> createCastByScript(@Valid @RequestBody ScriptCastCreationDTO castRequest) {
        return ApiResponse.of(SuccessCode._OK, castService.createCastByScript(castRequest));
    }

    /* Cast 저장 API */
    @PostMapping(value = "/{castId}", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    @Operation(summary = "캐스트 저장 API (저장 화면에서 호출)")
    public ApiResponse<Object> saveCast(@PathVariable("castId") Long castId,
                                        @Valid @RequestPart(value = "saveInfo") CastSaveDTO saveRequest,
                                        @RequestPart(value = "image", required = false) MultipartFile image) {
        Cast cast = castService.saveCast(castId, saveRequest, image);
        return ApiResponse.of(SuccessCode._OK, "저장되었습니다");
    }

    /* Cast 재생 API */
    @CrossOrigin
    @GetMapping("/{castId}")
    @Operation(summary = "캐스트 재생 API")
    public ApiResponse<Object> findCast(@PathVariable("castId") Long castId) {
        return ApiResponse.of(SuccessCode._OK, castService.findCast(castId));
    }

    /* Cast 수정 API */
    @PatchMapping("/{castId}")
    @Operation(summary = "캐스트 수정 API")
    public ApiResponse<Object> updateCast(@PathVariable("castId") Long castId,
                                          @Valid @RequestPart(value = "updateInfo") CastUpdateDTO updateRequest,
                                          @RequestPart(value = "image", required = false) MultipartFile image) {
        // TODO 캐스트 생성자 혹은 관리자여야 함
        Cast cast = castService.updateCast(castId, updateRequest, image);
        return ApiResponse.of(SuccessCode._OK, "수정되었습니다");
    }

    /* Cast 삭제 API */
    @DeleteMapping("/{castId}")
    @Operation(summary = "캐스트 삭제 API")
    public ApiResponse<Object> deleteCast(@PathVariable("castId") Long castId) {
        // TODO 캐스트 생성자 혹은 관리자여야 함
        return ApiResponse.of(SuccessCode._OK, castService.deleteCast(castId));
    }

    @GetMapping("/home")
    @Operation(summary = "홈 화면 키워드 6개 받아오기")
    public ApiResponse<List<String>> getHomeKeyword(@AuthUser Member member) {
        return ApiResponse.onSuccess(keywordService.createKeyword(member));
    }

    @CrossOrigin
    @Operation(summary = "검색 홈 API")
    @GetMapping("/search/home")
    public ApiResponse<List<CastHomeDTO>> searchHome(@AuthUser Member member) {
        return ApiResponse.onSuccess(castService.getHomeCast(member));
    }

    @CrossOrigin
    @Operation(summary = "다른 사람의 플레이리스트 가져오기")
    @PostMapping("/other")
    public ApiResponse<OtherCastResponseDTO> getOtherCast(@AuthUser Member member, @RequestBody OtherCastRequestDTO castDTO) {
        return ApiResponse.onSuccess(castService.getOtherCast(member, castDTO));
    }

    @CrossOrigin
    @Operation(summary = "검색 API")
    @PostMapping("/search")
    public ApiResponse<List<CastHomeDTO>> saveCast(@AuthUser Member member, @RequestParam("keyword") String keyword) {
        return ApiResponse.onSuccess(castService.getCast(member, keyword));
    }
}
