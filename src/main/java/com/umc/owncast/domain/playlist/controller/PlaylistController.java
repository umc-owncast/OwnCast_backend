package com.umc.owncast.domain.playlist.controller;

import com.umc.owncast.common.response.ApiResponse;
import com.umc.owncast.domain.member.annotation.AuthUser;
import com.umc.owncast.domain.member.entity.Member;
import com.umc.owncast.domain.playlist.dto.*;
import com.umc.owncast.domain.playlist.service.PlaylistService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "플레이리스트 API", description = "플레이리스트 관련 API입니다")
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class PlaylistController {

    private final PlaylistService playlistService;

    @CrossOrigin
    @Operation(summary = "플레이리스트 추가")
    @PostMapping("/playlist/{playlistName}")
    public ApiResponse<AddPlaylistDTO> addPlaylist(@AuthUser Member member, @PathVariable("playlistName") String playlistName) {
        return ApiResponse.onSuccess(playlistService.addPlaylist(member, playlistName));
    }

    @CrossOrigin
    @Operation(summary = "사용자의 플레이리스트 불러오기")
    @GetMapping("/playlist/view")
    public ApiResponse<List<PlaylistResultDTO>> getPlaylists(@AuthUser Member member) {
        return ApiResponse.onSuccess(playlistService.getAllPlaylists(member));
    }

    @Operation(summary = "플레이리스트 수정")
    @PatchMapping("/playlist/{playlistId}")
    public ApiResponse<ModifyPlaylistDTO> modifyPlaylist(@AuthUser Member member,
                                                         @PathVariable("playlistId") Long playlistId,
                                                         @RequestParam("playlistName") String playlistName) {
        return ApiResponse.onSuccess(playlistService.modifyPlaylist(member, playlistId, playlistName));
    }

    @CrossOrigin
    @Operation(summary = "플레이리스트 삭제")
    @DeleteMapping("/playlist")
    public ApiResponse<DeletePlaylistDTO> deletePlaylist(@AuthUser Member member, @RequestParam("playlistId") Long playlistId) {
        return ApiResponse.onSuccess(playlistService.deletePlaylist(member, playlistId));
    }

    @CrossOrigin
    @Operation(summary = "플레이리스트 조회")
    @GetMapping("/playlist/{playlistId}")
    public ApiResponse<GetPlaylistDTO> getPlaylist(@AuthUser Member member,
                                                    @PathVariable("playlistId") Long playlistId,
                                                    @RequestParam("page") Integer page,
                                                    @RequestParam("size") Integer size) {
        return ApiResponse.onSuccess(playlistService.getPlaylist(member, playlistId, page, size));
    }

    @CrossOrigin
    @Operation(summary = "내가 담아온 플레이리스트 조회")
    @GetMapping("/playlist/saved")
    public ApiResponse<GetPlaylistDTO> getSavedPlaylist(@AuthUser Member member) {
        return ApiResponse.onSuccess(playlistService.getAllSavedPlaylists(member)); // TODO PAGE로 크기 조절
    }

    @CrossOrigin
    @Operation(summary = "저장한 플레이리스트 조회")
    @GetMapping("/playlist/my")
    public ApiResponse<GetPlaylistDTO> getAllMyPlaylist(@AuthUser Member member) {
        return ApiResponse.onSuccess(playlistService.getAllMyPlaylists(member));
    }
}
