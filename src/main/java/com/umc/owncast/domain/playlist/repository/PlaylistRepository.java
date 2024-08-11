package com.umc.owncast.domain.playlist.repository;

import com.umc.owncast.domain.playlist.entity.Playlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.Optional;

public interface PlaylistRepository extends JpaRepository<Playlist, Long> {
    List<Playlist> findAllByMemberIdOrderByCreatedAt(@Param("memberId")Long memberId);
    @Query("SELECT p FROM Playlist p " +
            "WHERE p.name = '담아온 캐스트' AND p.id = :memberId")
    Optional<Playlist> findSavedPlaylist(Long memberId);

    boolean existsByNameAndMemberId(String name, Long memberId);

    Optional<Playlist> findByIdAndMemberId(Long id, Long memberId);

}
