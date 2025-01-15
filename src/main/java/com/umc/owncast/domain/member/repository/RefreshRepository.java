package com.umc.owncast.domain.member.repository;

import com.umc.owncast.domain.member.entity.Refresh;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshRepository extends JpaRepository<Refresh, Long> {
    Boolean existsByRefreshToken(String refreshToken);
    Optional<Refresh> findByRefreshToken(String refreshToken);

    void deleteByRefreshToken(String refreshToken);
}
