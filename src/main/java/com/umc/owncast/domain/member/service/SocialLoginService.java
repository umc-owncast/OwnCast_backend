package com.umc.owncast.domain.member.service;

import com.umc.owncast.common.oauth.util.PasswordUtil;
import com.umc.owncast.domain.enums.SocialType;
import com.umc.owncast.domain.member.dto.OAuthRequestDTO;
import com.umc.owncast.domain.member.entity.Member;
import com.umc.owncast.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class SocialLoginService implements UserDetailsService {

    private final MemberRepository memberRepository;

    private static final String KAKAO = "kakao";
    private static final String NAVER = "naver";

    @Override
    public UserDetails loadUserByUsername(String LoginId) throws UsernameNotFoundException {
        Member member = memberRepository.findByLoginId(LoginId)
                .orElseThrow(() -> new UsernameNotFoundException("해당 아이디가 존재하지 않습니다."));
        /*
        if (isDeletedUser(accountId)) {
            throw new NoSuchElementException("탈퇴한 유저입니다");
        }
        */

        return org.springframework.security.core.userdetails.User.builder()
                .username(member.getLoginId())
                .password(PasswordUtil.generateRandomPassword())
                .build();
    }

    public Member getOrCreateUser(OAuthRequestDTO attributes, SocialType socialType) {
        Member findMember = memberRepository.findBySocialTypeAndSocialId(socialType, attributes.getUserInfo().getId()).orElse(null);

        if (findMember == null) {
            Member saveMember = saveMember(attributes, socialType);
            return saveMember;
        }

        return findMember;
    }

    public SocialType getSocialType(String provider) {
        if (KAKAO.equals(provider)) {
            return SocialType.KAKAO;
        } else if (NAVER.equals(provider)) {
            return SocialType.NAVER;
        }
        return null;
    }

    private Member saveMember(OAuthRequestDTO attributes, SocialType socialType) {
        Member member = attributes.toEntity(socialType, attributes.getUserInfo());
        return memberRepository.save(member);
    }

    /*
    탈되유저 확인용
    private boolean isDeletedUser(String accountId) {
        return userRepository.existsByAccountIdAndIsDelUserTrue(accountId);
    }
     */

}
