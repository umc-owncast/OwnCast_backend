package com.umc.owncast.domain.member.service;

import com.umc.owncast.common.oauth.PublicKeyList;
import com.umc.owncast.common.oauth.kakao.KakaoClient;
import com.umc.owncast.common.oauth.util.IdTokenProvider;
import com.umc.owncast.domain.enums.SocialType;
import com.umc.owncast.domain.member.dto.OAuthRequestDTO;
import com.umc.owncast.domain.member.entity.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class KakaoService {
    @Value("${oauth2.kakao.client-id}")
    private String KAKAO_AUD;
    private static final String KAKAO_ISS = "https://kauth.kakao.com";
    private static final String CLAIM_ID = "sub";

    private final SocialLoginService socialLoginService;
    private final KakaoClient kakaoClient;
    private final IdTokenProvider idTokenProvider;

    @Value("${oauth2.kakao.redirect-url}")
    private String USER_INFO_URI;

    public Member getMemberInfo(String idToken, String provider) {

        // 토큰을 이용하여 사용자 정보 조회
        PublicKeyList kakaoPublicKeys = kakaoClient.getKakaoOIDCKeys();
        Map<String, Object> claims = new HashMap<>(
                idTokenProvider.extractClaims(idToken, KAKAO_AUD, KAKAO_ISS, kakaoPublicKeys)
        );
        SocialType socialType = socialLoginService.getSocialType(provider);
        OAuthRequestDTO extractAttributes = OAuthRequestDTO.of(socialType, claims.get(CLAIM_ID).toString(), claims);

        return socialLoginService.getOrCreateUser(extractAttributes, socialType);
    }
}
