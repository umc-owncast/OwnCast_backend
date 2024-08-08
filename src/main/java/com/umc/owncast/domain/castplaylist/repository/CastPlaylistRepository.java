package com.umc.owncast.domain.castplaylist.repository;

import com.umc.owncast.domain.castplaylist.entity.CastPlaylist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CastPlaylistRepository extends JpaRepository<CastPlaylist, Long> {
}
