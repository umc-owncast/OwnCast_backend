package com.umc.owncast.domain.playlist.repository;

import com.umc.owncast.domain.playlist.entity.Playlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PlaylistRepository extends JpaRepository<Playlist, Long> {
    @Query("SELECT p FROM Playlist p " +
            "WHERE p.name = '담아온 캐스트' AND p.id = :memberId")
    Optional<Playlist> findSavedPlaylist(@Param("memberId") Long memberId);

    @Query("SELECT p FROM Playlist p " +
            "JOIN CastPlaylist cp ON cp.playlist.id = p.id " +
            "WHERE cp.cast.id = :castId AND cp.cast.member.id = p.member.id ")
    Playlist findUserCategoryName(@Param("castId") Long castId);
}
