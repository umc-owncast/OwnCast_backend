package com.umc.owncast.domain.playlist.controller;


import com.umc.owncast.common.response.ApiResponse;
import com.umc.owncast.domain.bookmark.dto.BookMarkDTO;
import com.umc.owncast.domain.playlist.dto.PlaylistDTO;
import com.umc.owncast.domain.playlist.service.PlaylistService;
import com.umc.owncast.domain.playlist.service.PlaylistServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class PlaylistController {

    private final PlaylistServiceImpl playlistService;

    @CrossOrigin
    @Operation(summary = "사용자의 플레이리스트 불러오기")
    @GetMapping("/playlist")
    public ApiResponse<List<PlaylistDTO.PlaylistResultDTO>> getPlaylists() {
        return ApiResponse.onSuccess(playlistService.getAllPlaylists());
    }
}
