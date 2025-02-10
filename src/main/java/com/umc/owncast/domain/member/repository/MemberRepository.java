package com.umc.owncast.domain.member.repository;

import com.umc.owncast.domain.enums.SocialType;
import com.umc.owncast.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByLoginId(String LoginId);

    Optional<Member> findByNickname(String nickname);

    Optional<Member> findByUsername(String username);

    boolean existsByLoginId(String LoginId);

    boolean existsByNickname(String nickname);

    Optional<Member> findBySocialTypeAndSocialId(SocialType socialType, String socialId);


}
