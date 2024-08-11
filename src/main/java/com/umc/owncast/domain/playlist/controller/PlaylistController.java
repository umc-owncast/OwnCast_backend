package com.umc.owncast.domain.playlist.controller;

import com.umc.owncast.common.response.ApiResponse;
import com.umc.owncast.domain.playlist.dto.PlaylistDTO;
import com.umc.owncast.domain.playlist.service.PlaylistService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class PlaylistController {

//    private final PlaylistServiceImpl playlistService;
    private final PlaylistService playlistService;

    @CrossOrigin
    @Operation(summary = "카테고리 추가")
    @PostMapping("/playlist")
    public ApiResponse<PlaylistDTO.AddPlaylistDTO> addPlaylist(@RequestParam("playlistName") String playlistName) {
        return ApiResponse.onSuccess(playlistService.addPlaylist(playlistName));
    }

//    @CrossOrigin
//    @Operation(summary = "사용자의 플레이리스트 불러오기")
//    @GetMapping("/playlist")
//    public ApiResponse<List<PlaylistDTO.PlaylistResultDTO>> getPlaylists() {
//        return ApiResponse.onSuccess(playlistService.getAllPlaylists());
//    }

    @Operation(summary = "카테고리 수정")
    @PatchMapping("/playlist/{playlistId}")
    public ApiResponse<PlaylistDTO.ModifyPlaylistDTO> modifyPlaylist(@RequestParam("playlistId") Long playlistId,
                                                                     @RequestParam("playlistName") String playlistName) {
        return ApiResponse.onSuccess(playlistService.modifyPlaylist(playlistId, playlistName));
    }

    @CrossOrigin
    @Operation(summary = "카테고리 삭제")
    @DeleteMapping("/playlist/{playlistId}")
    public ApiResponse<PlaylistDTO.DeletePlaylistDTO> deletePlaylist(@RequestParam("playlistId") Long playlistId) {
        return ApiResponse.onSuccess(playlistService.deletePlaylist(playlistId));
    }

    @CrossOrigin
    @Operation(summary = "카테고리 조회")
    @GetMapping("/playlist/{playlistId}")
    public ApiResponse<PlaylistDTO.GetPlaylistDTO> getPlaylist(@RequestParam("playlistId") Long playlistId,
                                                               @RequestParam("page") Integer page,
                                                               @RequestParam("size") Integer size) {
        return ApiResponse.onSuccess(playlistService.getPlaylist(playlistId, page, size));
    }
}
