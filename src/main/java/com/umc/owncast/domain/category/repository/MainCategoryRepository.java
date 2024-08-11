package com.umc.owncast.domain.category.repository;

import com.umc.owncast.domain.category.entity.MainCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MainCategoryRepository extends JpaRepository<MainCategory, Long> {
}
