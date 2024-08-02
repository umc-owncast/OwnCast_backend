package com.umc.owncast.domain.castplaylist.repository;


import com.umc.owncast.domain.cast.entity.Cast;
import com.umc.owncast.domain.castplaylist.entity.CastPlaylist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CastPlaylistRepository extends JpaRepository<CastPlaylist, Long> {

    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END " +
            "FROM CastPlaylist c WHERE c.cast.id = :castId AND c.cast.member.id = :memberId ")
    boolean existsByMemberIdAndCastId(@Param("memberId") Long memberId, @Param("castId") Long castId);
}
