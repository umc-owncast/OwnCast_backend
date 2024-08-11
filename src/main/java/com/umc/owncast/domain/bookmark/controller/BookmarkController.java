package com.umc.owncast.domain.bookmark.controller;


import com.umc.owncast.common.response.ApiResponse;
import com.umc.owncast.domain.bookmark.dto.BookMarkDTO;
import com.umc.owncast.domain.bookmark.service.BookMarkServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class BookmarkController {

    private final BookMarkServiceImpl bookmarkService;

    @CrossOrigin
    @Operation(summary = "북마크된 문장 불러오기")
    @GetMapping("/study/{playlistId}")
    public ApiResponse<List<BookMarkDTO.BookMarkResultDTO>> getBookmarks(@PathVariable("playlistId") Long playlistId) {
        return ApiResponse.onSuccess(bookmarkService.getBookmarks(playlistId));
    }

    @CrossOrigin
    @Operation(summary = "내가 저장한 캐스트의 북마크된 문장 불러오기")
    @GetMapping("/study/mycast")
    public ApiResponse<List<BookMarkDTO.BookMarkResultDTO>> getMyCastBookmarks() {
        return ApiResponse.onSuccess(bookmarkService.getMyCastBookmarks());
    }

    @CrossOrigin
    @Operation(summary = "내가 저장한 남의 캐스트의 북마크된 문장 불러오기")
    @GetMapping("/study/savedcast")
    public ApiResponse<List<BookMarkDTO.BookMarkResultDTO>> getSavedBookmarks() {
        return ApiResponse.onSuccess(bookmarkService.getSavedBookmarks());
    }

    @CrossOrigin
    @Operation(summary = "문장 북마크 하기")
    @PostMapping("/bookmark")
    public ApiResponse<BookMarkDTO.BookMarkSaveResultDTO> saveBookmark(@RequestParam("sentenceId") Long sentenceId) {
        return ApiResponse.onSuccess(bookmarkService.saveBookmark(sentenceId));
    }

    @CrossOrigin
    @Operation(summary = "문장 북마크 취소하기")
    @DeleteMapping("/bookmark")
    public ApiResponse<BookMarkDTO.BookMarkSaveResultDTO> deleteBookmark(@RequestParam("sentenceId") Long sentenceId) {
        return ApiResponse.onSuccess(bookmarkService.deleteBookmark(sentenceId));
    }
}
