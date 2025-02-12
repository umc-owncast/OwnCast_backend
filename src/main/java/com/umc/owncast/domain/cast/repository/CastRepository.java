package com.umc.owncast.domain.cast.repository;

import com.umc.owncast.domain.cast.entity.Cast;
import com.umc.owncast.domain.enums.Language;
import com.umc.owncast.domain.enums.MainCategory;
import com.umc.owncast.domain.member.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CastRepository extends JpaRepository<Cast, Long> {

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
            "JOIN SubPrefer s ON s.member = c.member " +
            "JOIN SubCategory sub ON sub = s.subCategory " +
            "WHERE sub.mainCategory = :mainCategory " +
            "AND c.isPublic = true " +
            "AND c.member != :member " +
            "AND c.language = :language " +
            "ORDER BY c.hits DESC")
    Page<Cast> findTop5ByMainCategoryOrderByHitsDesc(@Param("mainCategory") MainCategory mainCategory,
                                                      @Param("member") Member member,
                                                      @Param("language") Language language,
                                                      Pageable pageable);
}
