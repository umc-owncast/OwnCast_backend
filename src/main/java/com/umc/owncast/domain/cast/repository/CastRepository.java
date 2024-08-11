package com.umc.owncast.domain.cast.repository;


import com.umc.owncast.domain.cast.entity.Cast;
import com.umc.owncast.domain.castcategory.entity.CastMainCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CastRepository extends JpaRepository<Cast, Long> {

    @Query(value = "SELECT * FROM `cast` WHERE MATCH(title) AGAINST(?) AND `is_public` = true ", nativeQuery = true)
    List<Cast> castSearch(@Param("text") String text);

    @Query("SELECT c FROM Cast c " +
            "JOIN MainPrefer m ON m.member.id = c.member.id " +
            "WHERE m.mainCategory.id = :mainCategoryId AND c.isPublic = true AND c.id != :memberId " +
            "ORDER BY c.hits DESC")
    Page<Cast> findTop5ByMainCategoryIdOrderByHitsDesc(@Param("mainCategoryId") Long mainCategoryId, @Param("pageable") Pageable pageable, @Param("memberId") long memberId);
}
