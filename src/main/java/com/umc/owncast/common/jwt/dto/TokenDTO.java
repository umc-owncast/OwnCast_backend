package com.umc.owncast.common.jwt.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Builder
@Data
@AllArgsConstructor
public class TokenDTO {
    private String accessToken;
    private String refreshToken;
    private String grantType;
}