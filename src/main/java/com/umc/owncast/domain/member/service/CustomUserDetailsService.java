package com.umc.owncast.domain.member.service;

import com.umc.owncast.domain.member.entity.Member;
import com.umc.owncast.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserById(String userId) throws UsernameNotFoundException {
        return memberRepository.findById(Long.parseLong(userId))
                .map(this::createUserDetails)
                .orElseThrow(() -> new UsernameNotFoundException("아이디 또는 비밀번호가 잘못 되었습니다. 다시 입력해 주세요."));
    }

    private UserDetails createUserDetails(Member member) {
        return member.builder()
                .id(member.getId())
                .password(passwordEncoder.encode(member.getPassword()))
                .roles(member.getRoles())
                .build();
    }
}
