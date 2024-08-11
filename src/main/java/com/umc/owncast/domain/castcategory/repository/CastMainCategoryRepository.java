package com.umc.owncast.domain.castcategory.repository;

import com.umc.owncast.domain.cast.entity.Cast;
import com.umc.owncast.domain.castcategory.entity.CastMainCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CastMainCategoryRepository extends JpaRepository<CastMainCategory, Long> {

    @Query("SELECT cmc FROM CastMainCategory cmc " +
            "JOIN cmc.cast c " +
            "WHERE cmc.mainCategory.id = :mainCategoryId " +
            "ORDER BY c.hits DESC")
    List<CastMainCategory> findTop5ByMainCategoryIdOrderByHitsDesc(Long mainCategoryId, Pageable pageable);

    CastMainCategory findByCastId(Long castId);
}
