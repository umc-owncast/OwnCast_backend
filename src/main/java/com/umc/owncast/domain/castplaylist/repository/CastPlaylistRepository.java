package com.umc.owncast.domain.castplaylist.repository;

import com.umc.owncast.domain.castplaylist.entity.CastPlaylist;
import com.umc.owncast.domain.playlist.entity.Playlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CastPlaylistRepository extends JpaRepository<CastPlaylist, Long> {

    @Query("SELECT cp FROM CastPlaylist cp JOIN cp.cast c JOIN Sentence s ON s.cast = c WHERE s.id = :sentenceId AND cp.playlist.member.id = :memberId")
    Optional<CastPlaylist> findBySentenceId(@Param("sentenceId") Long sentenceId, @Param("memberId") Long memberId);

    CastPlaylist findFirstByPlaylist_Member_IdOrderByCreatedAt(@Param("memberId") long memberId);

    long countAllByPlaylist(@Param("playlist") Playlist playlist);
}
