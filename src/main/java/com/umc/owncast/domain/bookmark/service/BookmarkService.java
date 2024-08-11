package com.umc.owncast.domain.bookmark.service;

import com.umc.owncast.domain.bookmark.dto.BookMarkDTO;

import java.util.List;

public interface BookmarkService {
    List<BookMarkDTO.BookMarkResultDTO> getBookmarks();

    BookMarkDTO.BookMarkSaveResultDTO saveBookmark(Long sentenceId);
}
