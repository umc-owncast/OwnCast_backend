package com.umc.owncast.domain.cast.repository;


import com.umc.owncast.domain.cast.entity.Cast;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CastRepository extends JpaRepository<Cast, Long> {

    @Query(value = "SELECT * FROM cast WHERE MATCH(title) AGAINST(?1)",  nativeQuery = true)
    List<Cast> castSearch(String text);
}
