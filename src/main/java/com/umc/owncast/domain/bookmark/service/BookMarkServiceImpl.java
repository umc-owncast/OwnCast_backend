package com.umc.owncast.domain.bookmark.service;

import com.umc.owncast.common.exception.handler.UserHandler;
import com.umc.owncast.common.response.status.ErrorCode;
import com.umc.owncast.domain.bookmark.Repository.BookmarkRepository;
import com.umc.owncast.domain.bookmark.dto.BookmarkResultDTO;
import com.umc.owncast.domain.bookmark.dto.BookmarkSaveResultDTO;
import com.umc.owncast.domain.bookmark.entity.Bookmark;
import com.umc.owncast.domain.cast.entity.Cast;
import com.umc.owncast.domain.cast.repository.CastRepository;
import com.umc.owncast.domain.castplaylist.entity.CastPlaylist;
import com.umc.owncast.domain.castplaylist.repository.CastPlaylistRepository;
import com.umc.owncast.domain.castplaylist.service.CastPlaylistService;
import com.umc.owncast.domain.sentence.entity.Sentence;
import com.umc.owncast.domain.sentence.service.SentenceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class BookMarkServiceImpl implements BookmarkService {
    private final BookmarkRepository bookmarkRepository;
    private final CastPlaylistRepository castPlaylistRepository;
    private final CastPlaylistService castPlaylistService;
    private final SentenceService sentenceService;
    private final CastRepository castRepository;

    @Override
    public List<BookmarkResultDTO> getMyCastBookmarks() {

        List<Cast> castList = castRepository.findCastsByMember_Id(1L);
        return getBookMarkResultDTOs(castList);
    }

    @Override
    public List<BookmarkResultDTO> getSavedBookmarks() {

        List<Cast> castList = castPlaylistRepository.findSavedCast(1L);

        return getBookMarkResultDTOs(castList);
    }

    @NotNull
    private List<BookmarkResultDTO> getBookMarkResultDTOs(List<Cast> castList) {
        List<BookmarkResultDTO> sentenceList = new ArrayList<>(List.of());

        castList.forEach(cast -> {
            List<Bookmark> bookmarks = bookmarkRepository.findBookmarksByCastPlaylist_Cast_Id(cast.getId());

            bookmarks.forEach(bookmark -> {

                List<Sentence> sentences = bookmarkRepository.findSentencesByBookmarkId(bookmark.getId());

                sentences.forEach(sentence -> sentenceList.add(BookmarkResultDTO.builder()
                        .castId(sentence.getCast().getId())
                        .originalSentence(sentence.getOriginalSentence())
                        .translatedSentence(sentence.getTranslatedSentence())
                        .build()));
            });
        });

        return sentenceList;
    }

    @Override
    public List<BookmarkResultDTO> getBookmarks(Long playlistId) {

        List<Sentence> sentenceList = bookmarkRepository.findSentencesByPlaylistId(playlistId);

        return sentenceList.stream().map(sentence ->
                BookmarkResultDTO.builder()
                        .castId(sentence.getCast().getId())
                        .originalSentence(sentence.getOriginalSentence())
                        .translatedSentence(sentence.getTranslatedSentence())
                        .build()
        ).toList();
    }

    @Override
    public BookmarkSaveResultDTO saveBookmark(Long sentenceId) {
        CastPlaylist castPlaylist = castPlaylistService.findBySentenceId(sentenceId, 1L);

        if (bookmarkRepository.findBookmarkBySentenceIdAndCastPlaylist_Playlist_Member_id(sentenceId, 1L).isPresent()) {
            throw new UserHandler(ErrorCode.BOOKMARK_ALREADY_EXIST);
        }

        Bookmark newBookmarks = Bookmark.builder()
                .castPlaylist(castPlaylist)
                .sentence(sentenceService.findById(sentenceId))
                .build();

        bookmarkRepository.save(newBookmarks);

        return BookmarkSaveResultDTO.builder()
                .bookmarkId(newBookmarks.getId())
                .build();
    }

    @Override
    public BookmarkSaveResultDTO deleteBookmark(Long sentenceId) {

        Optional<Bookmark> optionalBookmark = bookmarkRepository.findBookmarkBySentenceIdAndCastPlaylist_Playlist_Member_id(sentenceId, 1L);

        if (optionalBookmark.isPresent()) {
            bookmarkRepository.delete(optionalBookmark.get());
            return BookmarkSaveResultDTO.builder()
                    .bookmarkId(optionalBookmark.get().getId())
                    .build();
        } else {
            throw new UserHandler(ErrorCode.BOOKMARK_NOT_EXIST);
        }
    }

}
