package com.umc.owncast.domain.memberprefer.repository;

import com.umc.owncast.domain.member.entity.Member;
import com.umc.owncast.domain.memberprefer.entity.MainPrefer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MemberPreferRepository extends JpaRepository<MainPrefer, Long> {
    Optional<MainPrefer> findByMember(@Param("member") Member member);
}
