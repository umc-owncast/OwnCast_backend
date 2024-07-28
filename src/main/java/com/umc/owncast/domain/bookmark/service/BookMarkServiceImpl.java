package com.umc.owncast.domain.bookmark.service;

import com.umc.owncast.domain.bookmark.Repository.BookmarkRepository;
import com.umc.owncast.domain.bookmark.dto.BookMarkDTO;
import com.umc.owncast.domain.bookmark.entity.Bookmark;
import com.umc.owncast.domain.castplaylist.entity.CastPlaylist;
import com.umc.owncast.domain.castplaylist.repository.CastPlaylistRepository;
import com.umc.owncast.domain.playlist.entity.Playlist;
import com.umc.owncast.domain.sentence.entity.Sentence;
import com.umc.owncast.domain.sentence.repository.SentenceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class BookMarkServiceImpl {
    private final BookmarkRepository bookmarkRepository;
    private final CastPlaylistRepository castPlaylistRepository;
    private final SentenceRepository sentenceRepository;

    public List<BookMarkDTO.BookMarkResultDTO> getBookmarks(Long categoryId){
        List<Sentence> sentenceList = bookmarkRepository.findSentencesByPlaylistId(categoryId);

        return sentenceList.stream().map(sentence ->
                BookMarkDTO.BookMarkResultDTO.builder()
                        .castId(sentence.getCast().getId())
                        .originalSentence(sentence.getOriginalSentence())
                        .translatedSentence(sentence.getTranslatedSentence())
                        .build()
        ).toList();
    }

    public List<BookMarkDTO.BookMarkSaveResultDTO> saveBookmark(Long sentenceId) {
        List<CastPlaylist> castPlaylist = castPlaylistRepository.findBySentenceId(sentenceId, 1L);

        List<BookMarkDTO.BookMarkSaveResultDTO> bookmarkSaveResultDTOList = new ArrayList<>();

        List<Bookmark> newBookmarks = castPlaylist.stream().map(castplaylist ->
                Bookmark.builder()
                        .castPlaylist(castplaylist)
                        .sentence(sentenceRepository.findById(sentenceId).get())
                        .build()
        ).toList();

        newBookmarks.forEach(bookmark -> {
            Bookmark savedBookmark = bookmarkRepository.save(bookmark);
            BookMarkDTO.BookMarkSaveResultDTO resultDTO = BookMarkDTO.BookMarkSaveResultDTO.builder()
                    .bookmarkId(savedBookmark.getId())
                    .build();
            bookmarkSaveResultDTOList.add(resultDTO);
        });

        return bookmarkSaveResultDTOList;
    }

}
