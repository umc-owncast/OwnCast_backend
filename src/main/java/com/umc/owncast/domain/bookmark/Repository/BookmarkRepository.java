package com.umc.owncast.domain.bookmark.Repository;

import com.umc.owncast.domain.bookmark.entity.Bookmark;
import com.umc.owncast.domain.castplaylist.entity.CastPlaylist;
import com.umc.owncast.domain.sentence.entity.Sentence;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {
    @Query("SELECT b.sentence FROM Bookmark b WHERE b.castPlaylist.playlist.id = :playlistId")
    List<Sentence> findSentencesByPlaylistId(@Param("playlistId") Long playlistId);

    Optional<Bookmark> findByCastPlaylist(@Param("castPlaylist") CastPlaylist castPlaylist);

    Optional<Bookmark> findBySentenceId(@Param("sentenceId") Long sentenceId);
}
