package com.umc.owncast.domain.cast.repository;

import com.umc.owncast.domain.cast.entity.Cast;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CastRepository extends JpaRepository<Cast,Long> {
}
