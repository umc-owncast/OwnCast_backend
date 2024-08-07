package com.umc.owncast.domain.bookmark.service;

import com.umc.owncast.common.exception.handler.UserHandler;
import com.umc.owncast.common.response.status.ErrorCode;
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
import java.util.Optional;
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

    public BookMarkDTO.BookMarkSaveResultDTO saveBookmark(Long sentenceId) {

        Optional<CastPlaylist> optionalCastPlaylist = castPlaylistRepository.findBySentenceId(sentenceId, 1L);
        CastPlaylist castPlaylist;

        if(optionalCastPlaylist.isPresent()){
            castPlaylist = optionalCastPlaylist.get();
        } else {
            throw new UserHandler(ErrorCode._BAD_REQUEST);
        }

        if(bookmarkRepository.findByCastPlaylist(castPlaylist).isPresent()){
            throw new UserHandler(ErrorCode.BOOKMARK_ALREADY_EXIST);
        }

        Bookmark newBookmarks = Bookmark.builder()
                        .castPlaylist(castPlaylist)
                        .sentence(sentenceRepository.findById(sentenceId).get())
                        .build();

        bookmarkRepository.save(newBookmarks);

        return BookMarkDTO.BookMarkSaveResultDTO.builder()
                .bookmarkId(newBookmarks.getId())
                .build();
    }

    public BookMarkDTO.BookMarkSaveResultDTO deleteBookmark(Long sentenceId) {

        Optional<Bookmark> optionalBookmark = bookmarkRepository.findBySentenceId(sentenceId);

        if(optionalBookmark.isPresent()){
            bookmarkRepository.delete(optionalBookmark.get());
            return BookMarkDTO.BookMarkSaveResultDTO.builder()
                    .bookmarkId(optionalBookmark.get().getId())
                    .build();
        } else {
            throw new UserHandler(ErrorCode.BOOKMARK_NOT_EXIST);
        }
    }

}
