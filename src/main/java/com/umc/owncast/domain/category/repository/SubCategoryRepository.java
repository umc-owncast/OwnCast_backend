package com.umc.owncast.domain.category.repository;

import com.umc.owncast.domain.category.entity.SubCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubCategoryRepository extends JpaRepository<SubCategory, Long> {
}
