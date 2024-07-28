package com.umc.owncast.domain.bookmark.controller;


import com.umc.owncast.common.response.ApiResponse;
import com.umc.owncast.domain.bookmark.dto.BookMarkDTO;
import com.umc.owncast.domain.bookmark.service.BookMarkServiceImpl;
import com.umc.owncast.domain.bookmark.service.BookmarkService;
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
    @GetMapping("/study/{categoryId}")
    public ApiResponse<List<BookMarkDTO.BookMarkResultDTO>> getBookmarks(@PathVariable("categoryId") Long categoryId) {
        return ApiResponse.onSuccess(bookmarkService.getBookmarks(categoryId));
    }

    @CrossOrigin
    @Operation(summary = "문장 북마크 하기")
    @PostMapping("/bookmark")
    public ApiResponse<List<BookMarkDTO.BookMarkSaveResultDTO>> saveBookmark(@RequestParam("sentenceId") Long sentenceId) {
        return ApiResponse.onSuccess(bookmarkService.saveBookmark(sentenceId));
    }
}
