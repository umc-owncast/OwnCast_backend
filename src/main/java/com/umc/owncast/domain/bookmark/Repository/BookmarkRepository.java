package com.umc.owncast.domain.bookmark.Repository;

import com.umc.owncast.domain.bookmark.entity.Bookmark;
import com.umc.owncast.domain.sentence.entity.Sentence;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {

    @Query("SELECT b.sentence FROM Bookmark b WHERE b.castPlaylist.playlist.id = :playlistId")
    List<Sentence> findSentencesByPlaylistId(@Param("playlistId") Long playlistId);

    Optional<Bookmark> findBookmarkBySentenceIdAndCastPlaylist_Playlist_Member_id(@Param("sentenceId") Long sentenceId , @Param("memberId") Long memberId);

    List<Bookmark> findBookmarksByCastPlaylist_Cast_Id(@Param("castId") Long castId);

    @Query("SELECT b.sentence FROM Bookmark b WHERE b.id = :bookmarkId")
    List<Sentence> findSentencesByBookmarkId(@Param("bookmarkId") Long bookmarkId);
}
