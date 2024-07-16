package com.umc.owncast.domain.Member.repository;

import com.umc.owncast.domain.Member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
