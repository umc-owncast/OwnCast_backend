package com.umc.owncast.domain.cast.repository;


import com.umc.owncast.domain.cast.entity.Cast;
import com.umc.owncast.domain.category.entity.MainCategory;
import com.umc.owncast.domain.enums.Language;
import com.umc.owncast.domain.member.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CastRepository extends JpaRepository<Cast, Long> {

    @Query("SELECT c.imagePath FROM Cast c " +
            "WHERE c.member.id = :memberId " +
            "ORDER BY c.createdAt ASC ")
    Page<String> findFirstCastImage(@Param("memberId") Long memberId, Pageable pageable);

    List<Cast> findCastsByMember_Id(@Param("memberId") Long memberId);

    @Query(value = "SELECT * FROM `cast` " +
            "WHERE MATCH(title) AGAINST(:text IN BOOLEAN MODE) " +
            "AND `is_public` = true " +
            "AND `member_id` != :memberId " +
            "AND `language` = :language",
            nativeQuery = true)
    List<Cast> castSearch(@Param("text") String text,
                          @Param("memberId") Long memberId,
                          @Param("language") String language);

    @Query("SELECT c FROM Cast c " +
            "JOIN MainPrefer m ON m.member = c.member " +
            "WHERE m.mainCategory = :mainCategory " +
            "AND c.isPublic = true " +
            "AND c.member != :member " +
            "AND c.language = :language " +
            "ORDER BY c.hits DESC ")
    Page<Cast> findTop5ByMainCategoryIdOrderByHitsDesc(@Param("mainCategory") MainCategory mainCategory, @Param("member") Member member, @Param("language") Language language, Pageable pageable);
}
