package com.umc.owncast.domain.castplaylist.repository;

import com.umc.owncast.domain.castplaylist.entity.CastPlaylist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CastPlaylistRepository extends JpaRepository<CastPlaylist, Long> {
    Optional<CastPlaylist> findByCastIdAndPlaylistId(Long castId, Long id);
}
