package com.umc.owncast.domain.playlist.repository;

import com.umc.owncast.domain.playlist.entity.Playlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PlaylistRepository extends JpaRepository<Playlist, Long> {

    List<Playlist> findAllByMemberIdOrderByCreatedAt(@Param("memberId") Long memberId);

    boolean existsByNameAndMemberId(String name, Long memberId);

    Optional<Playlist> findByIdAndMemberId(Long id, Long memberId);

}
