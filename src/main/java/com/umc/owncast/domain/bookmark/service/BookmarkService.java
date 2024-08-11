package com.umc.owncast.domain.bookmark.service;

import com.umc.owncast.domain.bookmark.dto.BookMarkDTO;
import com.umc.owncast.domain.bookmark.dto.BookMarkDTO.BookMarkSaveResultDTO;

import java.util.List;

public interface BookmarkService {
    List<BookMarkDTO.BookMarkResultDTO> getMyCastBookmarks();
    List<BookMarkDTO.BookMarkResultDTO> getSavedBookmarks();
    List<BookMarkDTO.BookMarkResultDTO> getBookmarks(Long playlistId);
    BookMarkSaveResultDTO saveBookmark(Long sentenceId);
    BookMarkSaveResultDTO deleteBookmark(Long sentenceId);
}
