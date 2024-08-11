package com.umc.owncast.domain.member.dto;

import lombok.Builder;
import lombok.Getter;

public class MemberResponse {

    @Getter
    @Builder
    public static class refreshTokenDto {
        String refreshToken;
    }

}
