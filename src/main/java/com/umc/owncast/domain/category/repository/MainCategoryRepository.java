package com.umc.owncast.domain.category.repository;

import com.umc.owncast.domain.category.entity.MainCategory;
import com.umc.owncast.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MainCategoryRepository extends JpaRepository<MainCategory, Long> {
    @Query(" SELECT m.name " +
            " FROM MainCategory m " +
            " JOIN MainPrefer mp ON m.id = mp.mainCategory.id " +
            " WHERE mp.member = :member ")
    Optional<String> getMainCategoryNameByMember(@Param("member") Member member);

    Optional<MainCategory> findByName(String name);
}
