package com.umc.owncast.domain.member.repository;

import com.umc.owncast.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByLoginId(String LoginId);
    Optional<Member> findByNickname(String nickname);

    boolean existsByLoginId(String LoginId);
    boolean existsByNickname(String nickname);
}
