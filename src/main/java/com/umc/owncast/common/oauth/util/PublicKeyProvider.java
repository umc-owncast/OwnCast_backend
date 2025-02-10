package com.umc.owncast.common.oauth.util;

import com.umc.owncast.common.oauth.PublicKeyList;
import com.umc.owncast.common.oauth.dto.OIDCPublicKey;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;
import java.util.Base64;
import java.util.Map;


@Component
public class PublicKeyProvider {
    private static final String SIGN_ALGORITHM_HEADER = "alg";
    private static final String KEY_ID_HEADER = "kid";
    private static final int POSITIVE_SIGN_NUMBER = 1;

    public static PublicKey generate(Map<String, String> header, PublicKeyList<OIDCPublicKey> publicKeys) {
        final OIDCPublicKey OIDCPublicKey = publicKeys.getMatchingKey(
                header.get(SIGN_ALGORITHM_HEADER),
                header.get(KEY_ID_HEADER)
        );
        return generatePublicKey(OIDCPublicKey);
    }

    private static PublicKey generatePublicKey(OIDCPublicKey OIDCPublicKey) {
        final byte[] nBytes = Base64.getUrlDecoder().decode(OIDCPublicKey.getN());
        final byte[] eBytes = Base64.getUrlDecoder().decode(OIDCPublicKey.getE());

        final BigInteger n = new BigInteger(POSITIVE_SIGN_NUMBER, nBytes);
        final BigInteger e = new BigInteger(POSITIVE_SIGN_NUMBER, eBytes);
        final RSAPublicKeySpec keySpec = new RSAPublicKeySpec(n, e);

        try {
            final KeyFactory keyFactory = KeyFactory.getInstance(OIDCPublicKey.getKty());
            return keyFactory.generatePublic(keySpec);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException exception) {
            throw new RuntimeException("잘못된 퍼블릭 키입니다.");
        }

    }
}
