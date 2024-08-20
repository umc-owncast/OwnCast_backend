package com.umc.owncast.domain.castplaylist.repository;

import com.umc.owncast.domain.cast.entity.Cast;
import com.umc.owncast.domain.castplaylist.entity.CastPlaylist;
import com.umc.owncast.domain.playlist.entity.Playlist;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CastPlaylistRepository extends JpaRepository<CastPlaylist, Long> {

    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END " +
            "FROM CastPlaylist c WHERE c.playlist.member.id = :memberId AND c.cast.id = :castId ")
    boolean existsByMemberIdAndCastId(@Param("memberId") Long memberId, @Param("castId") Long castId);

    @Query("SELECT cp FROM CastPlaylist cp JOIN cp.cast c JOIN Sentence s ON s.cast = c WHERE s.id = :sentenceId AND cp.playlist.member.id = :memberId")
    Optional<CastPlaylist> findBySentenceId(@Param("sentenceId") Long sentenceId, @Param("memberId") Long memberId);

    @Query("SELECT cp.cast FROM CastPlaylist cp WHERE cp.cast.member.id != :memberId AND cp.playlist.member.id = :memberId ORDER BY cp.createdAt ASC")
    Page<Cast> findFirstOtherCast(@Param("memberId") long memberId, Pageable pageable);

    @Query("SELECT cp.cast FROM CastPlaylist cp WHERE cp.cast.member.id = :memberId AND cp.playlist.member.id = :memberId ORDER BY cp.createdAt ASC")
    Page<Cast> findFirstSavedCast(@Param("memberId") long memberId, Pageable pageable);

    long countAllByPlaylist(@Param("playlist") Playlist playlist);

    void deleteAllByPlaylistId(Long playlistId);

    void deleteAllByCastId(Long castId);

    @Query("SELECT cp.cast FROM CastPlaylist cp WHERE cp.playlist.member.id = :memberId AND cp.cast.member.id != :memberId")
    List<Cast> findSavedCast(@Param("memberId") long memberId);

    Optional<CastPlaylist> findByCastIdAndPlaylistId(Long castId, Long id);

    Page<CastPlaylist> findByPlaylistId(Long playlistId, Pageable pageable);

    List<CastPlaylist> findAllByPlaylistId(Long playlistId);

    Optional<CastPlaylist> findFirstByPlaylist_IdOrderByCreatedAt(@Param("playlistId") long playlistId);
}
