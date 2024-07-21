package com.umc.owncast.domain.cast.controller;

import com.umc.owncast.common.response.ApiResponse;
import com.umc.owncast.domain.cast.dto.CastDTO;
import com.umc.owncast.domain.cast.service.CastSaveServiceImpl;
import com.umc.owncast.domain.cast.service.CastSearchServiceImpl;
import com.umc.owncast.domain.member.dto.MemberLoginRequestDTO;
import com.umc.owncast.domain.member.dto.MemberResponseDTO;
import com.umc.owncast.domain.member.dto.MemberSignUpRequestDTO;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class CastController {

    private final CastSearchServiceImpl castSearchService;
    private final CastSaveServiceImpl castSaveService;

    @CrossOrigin
    @Operation(summary = "검색 홈 API")
    @GetMapping("/search/{page}")
    public ApiResponse<List<CastDTO.CastHomeDTO>> searchHome(@PathVariable Integer page) {
        return ApiResponse.onSuccess(castSearchService.getHomeCast(page));
    }

    @CrossOrigin
    @Operation(summary = "캐스트 저장")
    @GetMapping("/save/{castId}")
    public ApiResponse<String> saveCast(@PathVariable Long castId) {
        castSaveService.saveCast(castId);
        return ApiResponse.onSuccess("추가 성공!");
    }

    @CrossOrigin
    @Operation(summary = "검색 API")
    @PostMapping("/search")
    public ApiResponse<List<CastDTO.CastHomeDTO>> saveCast(@RequestParam("keyword") String keyword) {
        return ApiResponse.onSuccess(castSearchService.getCast(keyword));
    }
}
