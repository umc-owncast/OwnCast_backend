package com.umc.owncast.common.oauth.kakao;

import com.umc.owncast.common.oauth.PublicKeyList;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Getter
@NoArgsConstructor
public class KakaoPublicKeys implements PublicKeyList<KakaoOIDCPublicKey> {

    private List<KakaoOIDCPublicKey> publicKeys;

    public KakaoPublicKeys(List<KakaoOIDCPublicKey> publicKeys) {
        this.publicKeys = List.copyOf(publicKeys);
    }

    @Override
    public KakaoOIDCPublicKey getMatchingKey(String alg, String kid) {
        return publicKeys.stream()
                .filter(key -> key.isSameAlg(alg) && key.isSameKid(kid))
                .findFirst()
                .orElseThrow(()-> new RuntimeException("잘못된 토큰 형식입니다."));
    }
}
