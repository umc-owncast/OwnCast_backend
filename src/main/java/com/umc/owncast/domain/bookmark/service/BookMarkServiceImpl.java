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
import com.umc.owncast.domain.member.entity.Member;
import com.umc.owncast.domain.sentence.entity.Sentence;
import com.umc.owncast.domain.sentence.repository.SentenceRepository;
import com.umc.owncast.domain.sentence.service.SentenceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class BookMarkServiceImpl implements BookmarkService {
    private final BookmarkRepository bookmarkRepository;
    private final CastPlaylistRepository castPlaylistRepository;
    private final SentenceRepository sentenceRepository;
    private final SentenceService sentenceService;
    private final CastRepository castRepository;

    @Override
    public List<BookmarkResultDTO> getMyCastBookmarks(Member member) {

        List<Cast> castList = castRepository.findCastsByMember_Id(member.getId());
        return getBookMarkResultDTOs(member, castList);
    }

    @Override
    public List<BookmarkResultDTO> getSavedBookmarks(Member member) {

        List<Cast> castList = castPlaylistRepository.findSavedCast(member.getId());
        return getBookMarkResultDTOs(member, castList);
    }

    @NotNull
    private List<BookmarkResultDTO> getBookMarkResultDTOs(Member member, List<Cast> castList) {
        List<BookmarkResultDTO> sentenceList = new ArrayList<>(List.of());

        castList.forEach(cast -> {
            List<Bookmark> bookmarks = bookmarkRepository.findBookmarksByCastPlaylist_Cast_Id(member, cast.getId());

            bookmarks.forEach(bookmark -> {

                List<Sentence> sentences = bookmarkRepository.findSentencesByBookmarkId(bookmark.getId());

                sentences.forEach(sentence -> {

                    Sentence nextSentence = sentenceRepository.findById(sentence.getId() + 1)
                            .orElseGet(() -> Sentence.builder()
                                    .timePoint(null)
                                    .build());

                    if(!Objects.equals(nextSentence.getCast().getId(), sentence.getCast().getId())){
                        nextSentence = Sentence.builder()
                                .timePoint(null)
                                .build();
                    }

                    sentenceList.add(BookmarkResultDTO.builder()
                        .castId(sentence.getCast().getId())
                        .castURL(sentence.getCast().getFilePath())
                        .sentenceId(sentence.getId())
                        .originalSentence(sentence.getOriginalSentence())
                        .translatedSentence(sentence.getTranslatedSentence())
                        .start(sentence.getTimePoint())
                        .end(nextSentence.getTimePoint())
                        .build());
                    }
                );
            });
        });

        return sentenceList;
    }

    @Override
    public List<BookmarkResultDTO> getBookmarks(Member member, Long playlistId) {

        List<Sentence> sentenceList = bookmarkRepository.findSentencesByPlaylistId(playlistId);

        return sentenceList.stream().map(sentence -> {

            Sentence nextSentence = sentenceRepository.findById(sentence.getId() + 1)
                    .orElseGet(() -> Sentence.builder()
                            .timePoint(null)
                            .build());

            if(!Objects.equals(nextSentence.getCast().getId(), sentence.getCast().getId())){
                nextSentence = Sentence.builder()
                        .timePoint(null)
                        .build();
            }

            return BookmarkResultDTO.builder()
                    .castId(sentence.getCast().getId())
                    .castURL(sentence.getCast().getFilePath())
                    .sentenceId(sentence.getId())
                    .originalSentence(sentence.getOriginalSentence())
                    .translatedSentence(sentence.getTranslatedSentence())
                    .start(sentence.getTimePoint())
                    .end(nextSentence.getTimePoint())
                    .build();
        }).toList();
    }

    @Override
    public BookmarkSaveResultDTO saveBookmark(Member member, Long sentenceId) {

        CastPlaylist castPlaylist = castPlaylistRepository.findBySentenceId(sentenceId, member.getId()).orElseThrow(() -> new UserHandler(ErrorCode.CAST_PLAYLIST_NOT_FOUND));

        if (bookmarkRepository.findBookmarkBySentenceIdAndCastPlaylist_Playlist_Member_id(sentenceId, member.getId()).isPresent()) {
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
    public BookmarkSaveResultDTO deleteBookmark(Member member, Long sentenceId) {

        Bookmark bookmark = bookmarkRepository.findBookmarkBySentenceIdAndCastPlaylist_Playlist_Member_id(sentenceId, member.getId()).orElseThrow(() -> new UserHandler(ErrorCode.BOOKMARK_NOT_EXIST));

        bookmarkRepository.delete(bookmark);

        return BookmarkSaveResultDTO.builder()
                .bookmarkId(bookmark.getId())
                .build();
    }

}
