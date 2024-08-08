package com.umc.owncast.domain.memberprefer.entity;

import com.umc.owncast.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MainPreferRepository extends JpaRepository<MainPrefer,Long> {
    Optional<MainPrefer> findByMember(@Param("member") Member member);
}
