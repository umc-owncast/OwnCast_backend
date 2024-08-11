package com.umc.owncast.domain.sentence.repository;

import com.umc.owncast.domain.sentence.entity.Sentence;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SentenceRepository extends JpaRepository<Sentence, Long> {
    List<Sentence> findAllByCastIdOrderByTimePointAsc(Long castId);

    List<Sentence> findAllByCastId(Long castId);
}
