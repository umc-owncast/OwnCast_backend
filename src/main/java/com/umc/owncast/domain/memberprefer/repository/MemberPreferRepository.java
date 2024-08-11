package com.umc.owncast.domain.memberprefer.repository;

import com.umc.owncast.domain.memberprefer.entity.MainPrefer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberPreferRepository extends JpaRepository<MainPrefer, Long> {
}
