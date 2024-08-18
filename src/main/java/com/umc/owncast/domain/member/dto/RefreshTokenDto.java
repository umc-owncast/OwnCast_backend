package com.umc.owncast.domain.member.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RefreshTokenDto {
    Long memberId;
    String refreshToken;
}
