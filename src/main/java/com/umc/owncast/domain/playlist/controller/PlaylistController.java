package com.umc.owncast.domain.playlist.controller;

import com.umc.owncast.common.response.ApiResponse;
import com.umc.owncast.domain.playlist.dto.PlaylistDTO;
import com.umc.owncast.domain.playlist.service.PlaylistAddServiceImpl;
import com.umc.owncast.domain.playlist.service.PlaylistDeleteServiceImpl;
import com.umc.owncast.domain.playlist.service.PlaylistModifyServiceImpl;
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
    private final PlaylistModifyServiceImpl PlaylistModifyService;
    private final PlaylistDeleteServiceImpl PlaylistDeleteService;

    @CrossOrigin
    @Operation(summary = "카테고리 추가")
    @PostMapping("/playlist")
    public ApiResponse<PlaylistDTO.AddPlaylistDTO> addPlaylist(@RequestParam("categoryName") String categoryName) {
        return ApiResponse.onSuccess(PlaylistAddService.addPlaylist(categoryName));
    }

    @CrossOrigin
    @Operation(summary = "카테고리 수정")
    @PatchMapping("/playlist/{playlistId}")
    public ApiResponse<PlaylistDTO.ModifyPlaylistDTO> modifyPlaylist(@RequestParam("categoryId") Long categoryId,
                                                                     @RequestParam("categoryName") String categoryName) {
        return ApiResponse.onSuccess(PlaylistModifyService.modifyPlaylist(categoryId, categoryName));
    }

    @CrossOrigin
    @Operation(summary = "카테고리 삭제")
    @DeleteMapping("/playlist/{playlistId}")
    public ApiResponse<PlaylistDTO.DeletePlaylistDTO> deletePlaylist(@RequestParam("playlistId") Long playlistId) {
        return ApiResponse.onSuccess(PlaylistDeleteService.deletePlaylist(playlistId));
    }

}
