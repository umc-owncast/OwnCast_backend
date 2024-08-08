package com.umc.owncast.domain.member.service;

import com.umc.owncast.domain.member.dto.MemberResponse;
import com.umc.owncast.domain.member.entity.Member;

public class MemberMapper {
    public static Member toLoginIdMember(String loginId, String encodedPassword) {
        return Member.builder()
                .loginId(loginId)
                .password(encodedPassword)
                .build();
    }

    public static MemberResponse.refreshTokenDto toRefreshToken(String refreshToken) {
        return MemberResponse.refreshTokenDto.builder()
                .refreshToken(refreshToken)
                .build();
    }
}
