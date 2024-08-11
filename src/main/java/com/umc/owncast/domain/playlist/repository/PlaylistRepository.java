package com.umc.owncast.domain.playlist.repository;

import com.umc.owncast.domain.playlist.entity.Playlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PlaylistRepository extends JpaRepository<Playlist, Long> {
    List<Playlist> findAllByMemberIdOrderByCreatedAt(@Param("memberId")Long memberId);
}
