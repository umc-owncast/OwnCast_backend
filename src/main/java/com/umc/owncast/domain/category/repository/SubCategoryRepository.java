package com.umc.owncast.domain.category.repository;

import com.umc.owncast.domain.category.entity.SubCategory;
import com.umc.owncast.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface SubCategoryRepository extends JpaRepository<SubCategory, Long> {

    @Query(" SELECT s " +
            " FROM SubCategory s " +
            " JOIN SubPrefer sp ON s.id =sp.subCategory.id " +
            " WHERE sp.member = :member ")
    Optional<SubCategory> getSubCategoryNameByMember(@Param("member") Member member);

    Optional<SubCategory> findByName(String name);
}
