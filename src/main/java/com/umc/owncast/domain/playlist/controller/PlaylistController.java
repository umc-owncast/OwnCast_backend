package com.umc.owncast.domain.playlist.controller;

import com.umc.owncast.common.response.ApiResponse;
import com.umc.owncast.domain.playlist.dto.PlaylistDTO;
import com.umc.owncast.domain.playlist.service.PlaylistAddServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class PlaylistController {

    private final PlaylistAddServiceImpl PlaylistAddService;

    @CrossOrigin
    @Operation(summary = "카테고리 추가")
    @PostMapping("/playlist-add")
    public ApiResponse<PlaylistDTO.AddPlaylistDTO> addPlaylist(@RequestParam("categoryName") String categoryName) {
        return ApiResponse.onSuccess(PlaylistAddService.addPlaylist(categoryName));
    }
}
