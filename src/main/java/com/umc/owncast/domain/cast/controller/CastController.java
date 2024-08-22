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
    public ApiResponse<CastScriptDTO> createCastByKeyword(@Valid @RequestBody KeywordCastCreationDTO castRequest,
                                                   @AuthUser Member member) {
        System.out.println("GET api/cast/keyword");
        return ApiResponse.of(SuccessCode._OK, castService.createCastByKeyword(castRequest, member));
    }

    /* Cast 생성 API (script) */
    @PostMapping("/script")
    @Operation(summary = "스크립트로 캐스트를 생성하는 API.")
    public ApiResponse<CastScriptDTO> createCastByScript(@Valid @RequestBody ScriptCastCreationDTO castRequest,
                                                  @AuthUser Member member) {
        System.out.println("GET api/cast/script");
        return ApiResponse.of(SuccessCode._OK, castService.createCastByScript(castRequest, member));
    }

    /* Cast 저장 API */
    @PostMapping(value = "/{castId}", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    @Operation(summary = "캐스트 저장 API (저장 화면에서 호출)")
    public ApiResponse<SimpleCastDTO> saveCast(@PathVariable("castId") Long castId,
                                        @ModelAttribute CastUpdateRequestDTO saveRequest,
                                        @AuthUser Member member) {
        System.out.println("POST api/cast/" + castId);
        SimpleCastDTO castDTO = castService.saveCast(castId, saveRequest, member);
        return ApiResponse.of(SuccessCode._CAST_SAVED, castDTO);
    }

    /* Cast 재생 API */
    @CrossOrigin
    @GetMapping("/{castId}")
    @Operation(summary = "캐스트 재생 API")
    public ApiResponse<CastDTO> findCast(@PathVariable("castId") Long castId,
                                        @AuthUser Member member) {
        System.out.println("GET api/cast/" + castId);
        return ApiResponse.of(SuccessCode._OK, castService.findCast(castId, member));
    }

    /* Cast 수정 API */
    @PatchMapping("/{castId}")
    @Operation(summary = "캐스트 수정 API")
    public ApiResponse<SimpleCastDTO> updateCast(@PathVariable("castId") Long castId,
                                          @ModelAttribute CastUpdateRequestDTO updateRequest,
                                          @AuthUser Member member) {
        System.out.println("PATCH api/cast/" + castId);
        SimpleCastDTO castDTO = castService.updateCast(castId, updateRequest, member);
        return ApiResponse.of(SuccessCode._CAST_UPDATED, castDTO);
    }

    /* Cast 삭제 API */
    @DeleteMapping("/{castId}")
    @Operation(summary = "캐스트 삭제 API")
    public ApiResponse<SimpleCastDTO> deleteCast(@PathVariable("castId") Long castId,
                                          @AuthUser Member member) {
        System.out.println("DELETE api/cast/" + castId);
        return ApiResponse.of(SuccessCode._OK, castService.deleteCast(castId, member));
    }

    @GetMapping("/home")
    @Operation(summary = "홈 화면 키워드 6개 받아오기")
    public ApiResponse<List<String>> getHomeKeyword(@AuthUser Member member) {
        System.out.println("GET api/cast/home");
        return ApiResponse.onSuccess(keywordService.createKeyword(member));
    }

    @CrossOrigin
    @Operation(summary = "검색 홈 API")
    @GetMapping("/search/home")
    public ApiResponse<List<CastHomeDTO>> searchHome(@AuthUser Member member) {
        System.out.println("GET api/cast/search/home");
        return ApiResponse.onSuccess(castService.getHomeCast(member));
    }

    @CrossOrigin
    @Operation(summary = "다른 사람의 플레이리스트 가져오기")
    @PostMapping("/other")
    public ApiResponse<OtherCastResponseDTO> getOtherCast(@AuthUser Member member, @RequestBody OtherCastRequestDTO castDTO) {
        System.out.println("POST api/cast/search/other");
        return ApiResponse.onSuccess(castService.getOtherCast(member, castDTO));
    }

    @CrossOrigin
    @Operation(summary = "검색 API")
    @PostMapping("/search")
    public ApiResponse<List<CastHomeDTO>> saveCast(@AuthUser Member member, @RequestParam("keyword") String keyword) {
        System.out.println("POST api/cast/search");
        return ApiResponse.onSuccess(castService.getCast(member, keyword));
    }
}
