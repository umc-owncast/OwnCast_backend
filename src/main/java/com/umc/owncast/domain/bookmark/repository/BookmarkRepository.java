package com.umc.owncast.domain.bookmark.repository;

import com.umc.owncast.domain.bookmark.entity.Bookmark;
import com.umc.owncast.domain.member.entity.Member;
import com.umc.owncast.domain.sentence.entity.Sentence;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {

    @Query("SELECT b.sentence FROM Bookmark b WHERE b.castPlaylist.playlist.id = :playlistId ORDER BY b.createdAt DESC ")
    List<Sentence> findSentencesByPlaylistId(@Param("playlistId") Long playlistId);

    Optional<Bookmark> findBookmarkBySentenceIdAndCastPlaylist_Playlist_Member_idOrderByCreatedAt(@Param("sentenceId") Long sentenceId, @Param("memberId") Long memberId);

    @Query("SELECT b FROM Bookmark b WHERE b.castPlaylist.cast.id = :castId AND b.castPlaylist.playlist.member = :member ORDER BY b.createdAt DESC ")
    List<Bookmark> findBookmarksByCastPlaylist_Cast_Id(@Param("member") Member member, @Param("castId") Long castId);

    @Query("SELECT b.sentence FROM Bookmark b WHERE b.id = :bookmarkId ORDER BY b.createdAt DESC ")
    List<Sentence> findSentencesByBookmarkId(@Param("bookmarkId") Long bookmarkId);
}
