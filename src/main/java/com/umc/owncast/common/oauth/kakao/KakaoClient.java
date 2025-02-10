package com.umc.owncast.common.oauth.kakao;

import com.umc.owncast.common.config.FeignConfig;
import com.umc.owncast.common.oauth.dto.OIDCPublicKey;
import com.umc.owncast.common.oauth.dto.OauthTokenResponse;
import feign.Headers;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "KakaoClient", url = "https://kauth.kakao.com", configuration = FeignConfig.class)
public interface KakaoClient {

    @Cacheable(cacheNames = "KakaoOICD", cacheManager = "oidcKeyCacheManager")
    @GetMapping("/.well-known/jwks.json")
    KakaoPublicKeys getKakaoOIDCKeys();
}
