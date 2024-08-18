package com.umc.owncast.domain.member.service;

import com.umc.owncast.domain.member.dto.RefreshTokenDto;
import com.umc.owncast.domain.member.entity.Member;

public class MemberMapper {
    public static Member toLoginIdMember(String loginId, String encodedPassword, String nickname, String username) {
        return Member.builder()
                .loginId(loginId)
                .password(encodedPassword)
                .nickname(nickname)
                .username(username)
                .build();
    }

    public static RefreshTokenDto toRefreshToken(String refreshToken) {
        return RefreshTokenDto.builder()
                .refreshToken(refreshToken)
                .build();
    }
}
