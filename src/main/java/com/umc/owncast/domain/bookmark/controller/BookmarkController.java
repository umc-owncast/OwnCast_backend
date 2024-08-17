package com.umc.owncast.domain.bookmark.controller;

import com.umc.owncast.common.response.ApiResponse;
import com.umc.owncast.domain.bookmark.dto.BookmarkResultDTO;
import com.umc.owncast.domain.bookmark.dto.BookmarkSaveResultDTO;
import com.umc.owncast.domain.bookmark.service.BookmarkService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "북마크 API", description = "북마크 관련 API입니다")
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class BookmarkController {

    private final BookmarkService bookmarkService;

    @CrossOrigin
    @Operation(summary = "북마크된 문장 불러오기")
    @GetMapping("/study/{playlistId}")
    public ApiResponse<List<BookmarkResultDTO>> getBookmarks(@PathVariable("playlistId") Long playlistId) {
        return ApiResponse.onSuccess(bookmarkService.getBookmarks(playlistId));
    }

    @CrossOrigin
    @Operation(summary = "내가 저장한 캐스트의 북마크된 문장 불러오기")
    @GetMapping("/study/mycast")
    public ApiResponse<List<BookmarkResultDTO>> getMyCastBookmarks() {
        return ApiResponse.onSuccess(bookmarkService.getMyCastBookmarks());
    }

    @CrossOrigin
    @Operation(summary = "내가 저장한 남의 캐스트의 북마크된 문장 불러오기")
    @GetMapping("/study/savedcast")
    public ApiResponse<List<BookmarkResultDTO>> getSavedBookmarks() {
        return ApiResponse.onSuccess(bookmarkService.getSavedBookmarks());
    }

    @CrossOrigin
    @Operation(summary = "문장 북마크 하기")
    @PostMapping("/bookmark")
    public ApiResponse<BookmarkSaveResultDTO> saveBookmark(@RequestParam("sentenceId") Long sentenceId) {
        return ApiResponse.onSuccess(bookmarkService.saveBookmark(sentenceId));
    }

    @CrossOrigin
    @Operation(summary = "문장 북마크 취소하기")
    @DeleteMapping("/bookmark")
    public ApiResponse<BookmarkSaveResultDTO> deleteBookmark(@RequestParam("sentenceId") Long sentenceId) {
        return ApiResponse.onSuccess(bookmarkService.deleteBookmark(sentenceId));
    }
}
