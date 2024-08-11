package com.umc.owncast.domain.cast.repository;


import com.umc.owncast.domain.cast.entity.Cast;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CastRepository extends JpaRepository<Cast, Long> {
    Cast findFirstByMemberIdOrderByCreatedAt(@Param("memberId") Long memberId);

    List<Cast> findCastsByMember_Id(@Param("memberId") Long memberId);

    @Query(value = "SELECT * FROM `cast` WHERE MATCH(title) AGAINST(:text) AND `is_public` = true AND `member_id` != :memberId ", nativeQuery = true)
    List<Cast> castSearch(@Param("text") String text, @Param("memberId") Long memberId);

    @Query("SELECT c FROM Cast c " +
            "JOIN MainPrefer m ON m.member.id = c.member.id " +
            "WHERE m.mainCategory.id = :mainCategoryId AND c.isPublic = true AND c.member.id != :memberId " +
            "ORDER BY c.hits DESC")
    Page<Cast> findTop5ByMainCategoryIdOrderByHitsDesc(@Param("mainCategoryId") Long mainCategoryId, @Param("pageable") Pageable pageable, @Param("memberId") long memberId);
}
