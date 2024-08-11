package com.umc.owncast.domain.language.repository;

import com.umc.owncast.domain.language.entity.Language;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LanguageRepository extends JpaRepository<Language, Long> {
}
