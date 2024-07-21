package com.umc.owncast.domain.sentence.repository;


import com.umc.owncast.domain.castplaylist.entity.CastPlaylist;
import com.umc.owncast.domain.sentence.entity.Sentence;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SentenceRepository extends JpaRepository<Sentence, Long> {
    List<Sentence> findAllByCastId(Long castId);
}
