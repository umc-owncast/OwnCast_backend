package com.umc.owncast.domain.bookmark.service;

import com.umc.owncast.domain.bookmark.dto.BookmarkResultDTO;
import com.umc.owncast.domain.bookmark.dto.BookmarkSaveResultDTO;
import com.umc.owncast.domain.member.entity.Member;

import java.util.List;

public interface BookmarkService {
    List<BookmarkResultDTO> getMyCastBookmarks(Member member);
    List<BookmarkResultDTO> getSavedBookmarks(Member member);
    List<BookmarkResultDTO> getBookmarks(Member member, Long playlistId);
    BookmarkSaveResultDTO saveBookmark(Member member, Long sentenceId);
    BookmarkSaveResultDTO deleteBookmark(Member member, Long sentenceId);
}
