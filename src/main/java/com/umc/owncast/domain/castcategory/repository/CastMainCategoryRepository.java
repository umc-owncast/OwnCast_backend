package com.umc.owncast.domain.castcategory.repository;

import com.umc.owncast.domain.cast.entity.Cast;
import com.umc.owncast.domain.castcategory.entity.CastMainCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CastMainCategoryRepository extends JpaRepository<CastMainCategory, Long> {
    CastMainCategory findByCastId(Long castId);
}
