package com.umc.owncast.domain.category.repository;

import com.umc.owncast.domain.category.entity.MainCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MainCategoryRepository extends JpaRepository<MainCategory, Long> {
    @Query(" SELECT m.name " +
            " FROM MainCategory m " +
            " JOIN MainPrefer mp ON m.id = mp.mainCategory.id " +
            " WHERE mp.member.id = :memberId ")
    Optional<String> getMainCategoryNameByMemberId(@Param("memberId") long memberId);

    Optional<MainCategory> findByName(String name);
}
