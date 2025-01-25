package com.umc.owncast.domain.playlist.controller;

import com.umc.owncast.common.annotation.TrackExecutionTime;
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
@TrackExecutionTime
public class PlaylistController {

    private final PlaylistService playlistService;

    @CrossOrigin
    @Operation(summary = "플레이리스트 추가")
    @PostMapping("/playlist/{playlistName}")
    public ApiResponse<AddPlaylistDTO> addPlaylist(@AuthUser Member member, @PathVariable("playlistName") String playlistName) {

        log.info("POST /api/playlist/{}");

        return ApiResponse.onSuccess(playlistService.addPlaylist(member, playlistName));
    }

    @CrossOrigin
    @Operation(summary = "사용자의 플레이리스트 불러오기")
    @GetMapping("/playlist/view")
    public ApiResponse<List<PlaylistResultDTO>> getPlaylists(@AuthUser Member member) {

        log.info("GET /api/playlist/view");

        return ApiResponse.onSuccess(playlistService.getAllPlaylists(member));
    }

    @Operation(summary = "플레이리스트 수정")
    @PatchMapping("/playlist/{playlistId}")
    public ApiResponse<ModifyPlaylistDTO> modifyPlaylist(@AuthUser Member member,
                                                         @PathVariable("playlistId") Long playlistId,
                                                         @RequestParam("playlistName") String playlistName) {
        System.out.println("PATCH /api/playlist/" + playlistId);
        return ApiResponse.onSuccess(playlistService.modifyPlaylist(member, playlistId, playlistName));
    }

    @CrossOrigin
    @Operation(summary = "플레이리스트 삭제")
    @DeleteMapping("/playlist")
    public ApiResponse<DeletePlaylistDTO> deletePlaylist(@AuthUser Member member, @RequestParam("playlistId") Long playlistId) {
        System.out.println("DELETE /api/playlist/" + playlistId);
        return ApiResponse.onSuccess(playlistService.deletePlaylist(member, playlistId));
    }

    @CrossOrigin
    @Operation(summary = "플레이리스트 조회")
    @GetMapping("/playlist/{playlistId}")
    public ApiResponse<GetPlaylistDTO> getPlaylist(@AuthUser Member member,
                                                    @PathVariable("playlistId") Long playlistId,
                                                    @RequestParam("page") Integer page,
                                                    @RequestParam("size") Integer size) {
        System.out.println("GET /api/playlist/" + playlistId);
        return ApiResponse.onSuccess(playlistService.getPlaylist(member, playlistId, page, size));
    }

    @CrossOrigin
    @Operation(summary = "내가 담아온 플레이리스트 조회")
    @GetMapping("/playlist/saved")
    public ApiResponse<GetPlaylistDTO> getSavedPlaylist(@AuthUser Member member) {
        System.out.println("POST /api/playlist/saved");
        return ApiResponse.onSuccess(playlistService.getAllSavedPlaylists(member)); // TODO PAGE로 크기 조절
    }

    @CrossOrigin
    @Operation(summary = "저장한 플레이리스트 조회")
    @GetMapping("/playlist/my")
    public ApiResponse<GetPlaylistDTO> getAllMyPlaylist(@AuthUser Member member) {
        System.out.println("POST /api/playlist/my");
        return ApiResponse.onSuccess(playlistService.getAllMyPlaylists(member));
    }

    @CrossOrigin
    @DeleteMapping("/playlist/{playlistId}/cast/{castId}")
    @Operation(summary = "플레이리스트에 남의 캐스트 삭제")
    public ApiResponse<DeleteCastFromPlaylistDTO> deleteCastFromPlaylist
            (@PathVariable("playlistId") Long playlistId,
             @PathVariable("castId") Long castId,
             @AuthUser Member member) {
        return ApiResponse.onSuccess(playlistService.deleteCast(playlistId, castId, member));
    }
}
