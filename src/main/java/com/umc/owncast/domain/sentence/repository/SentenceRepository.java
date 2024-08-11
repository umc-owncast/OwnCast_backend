package com.umc.owncast.domain.sentence.repository;

import com.umc.owncast.domain.sentence.entity.Sentence;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SentenceRepository extends JpaRepository<Sentence, Long> {
}
