package com.umc.owncast.common.oauth.kakao;

import com.umc.owncast.common.oauth.dto.OIDCPublicKey;

public class KakaoOIDCPublicKey extends OIDCPublicKey {
    public KakaoOIDCPublicKey(String kty, String kid, String alg, String use, String n, String e) {
        super(kty, kid, alg, use, n, e);
    }
}
