package com.umc.owncast.domain.bookmark.service;

import com.umc.owncast.domain.bookmark.dto.BookmarkResultDTO;
import com.umc.owncast.domain.bookmark.dto.BookmarkSaveResultDTO;

import java.util.List;

public interface BookmarkService {
    List<BookmarkResultDTO> getMyCastBookmarks();
    List<BookmarkResultDTO> getSavedBookmarks();
    List<BookmarkResultDTO> getBookmarks(Long playlistId);
    BookmarkSaveResultDTO saveBookmark(Long sentenceId);
    BookmarkSaveResultDTO deleteBookmark(Long sentenceId);
}
