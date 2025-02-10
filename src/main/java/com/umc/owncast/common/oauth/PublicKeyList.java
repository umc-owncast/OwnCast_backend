package com.umc.owncast.common.oauth;

import com.umc.owncast.common.oauth.dto.OIDCPublicKey;

import java.util.List;

public interface PublicKeyList<T extends OIDCPublicKey> {
    List<T> getPublicKeys();

    T getMatchingKey(final String alg, final String kid);
}
