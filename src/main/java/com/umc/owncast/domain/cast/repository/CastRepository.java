package com.umc.owncast.domain.cast.repository;

import com.umc.owncast.domain.cast.entity.Cast;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CastRepository extends JpaRepository<Cast, Long> {
    Cast findFirstByMemberIdOrderByCreatedAt(@Param("memberId") Long memberId);

    List<Cast> findCastsByMember_Id(@Param("memberId") Long memberId);
}

