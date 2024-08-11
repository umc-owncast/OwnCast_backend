package com.umc.owncast.domain.castplaylist.repository;


import com.umc.owncast.common.exception.handler.UserHandler;
import com.umc.owncast.common.response.status.ErrorCode;
import com.umc.owncast.domain.cast.entity.Cast;
import com.umc.owncast.domain.castplaylist.entity.CastPlaylist;
import com.umc.owncast.domain.playlist.entity.Playlist;
import com.umc.owncast.domain.playlist.repository.PlaylistRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CastPlaylistRepository extends JpaRepository<CastPlaylist, Long> {

    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END " +
            "FROM CastPlaylist c WHERE c.playlist.id = :playlistId AND c.cast.id = :castId")
    boolean existsByPlaylistIdAndCastId(@Param("playlistId") Long playlistId, @Param("castId") Long castId);

    void deleteAllByPlaylistId(Long playlistId);

    Page<CastPlaylist> findByPlaylistId(Long playlistId, Pageable pageable);
}
