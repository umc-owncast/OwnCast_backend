package com.umc.owncast.domain.memberprefer.repository;

import com.umc.owncast.domain.member.entity.Member;
import com.umc.owncast.domain.memberprefer.entity.SubPrefer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface SubPreferRepository extends JpaRepository<SubPrefer, Long> {
    Optional<SubPrefer> findByMember(@Param("member") Member member);
}
