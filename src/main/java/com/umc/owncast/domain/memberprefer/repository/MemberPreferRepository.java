package com.umc.owncast.domain.memberprefer.repository;

import com.umc.owncast.domain.memberprefer.entity.MainPrefer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberPreferRepository extends JpaRepository<MainPrefer, Long> {
    Optional<MainPrefer> findByMemberId(long l);
}
