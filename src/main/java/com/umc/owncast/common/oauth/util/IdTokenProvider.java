package com.umc.owncast.common.oauth.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.umc.owncast.common.oauth.PublicKeyList;
import com.umc.owncast.common.oauth.dto.OIDCPublicKey;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.Map;

@Component
@Slf4j
@RequiredArgsConstructor
public class IdTokenProvider {

    private static final String ID_TOKEN_VALUE_DELIMITER = "\\.";
    private static final int HEADER_INDEX = 0;
    private static final int PAYLOAD_INDEX = 1;

    private final ObjectMapper objectMapper;

    private Map<String, String> parseHeader(final String idToken) {
        try {
            final String encodedHeader = idToken.split(ID_TOKEN_VALUE_DELIMITER)[HEADER_INDEX];
            final String decodedHeader = new String(Base64.getUrlDecoder().decode(encodedHeader));
            return objectMapper.readValue(decodedHeader, Map.class);
        } catch (JsonMappingException e) {
            throw new RuntimeException("idToken 값이 jwt 형식인지, 값이 정상적인지 확인해주세요.");
        } catch (JsonProcessingException e) {
            throw new RuntimeException("디코드된 헤더를 Map 형태로 분류할 수 없습니다. 헤더를 확인해주세요.");
        }
    }

    public Claims extractClaims(String idToken, String aud, String iss, PublicKeyList<OIDCPublicKey> publicKeys) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(PublicKeyProvider.generate(parseHeader(idToken), publicKeys))
                    .requireAudience(aud)
                    .requireIssuer(iss)
                    .build()
                    .parseClaimsJws(idToken)
                    .getBody();
            /*
            return Jwts.parser()
                    .verifyWith(PublicKeyProvider.generate(parseHeader(idToken), publicKeys))
                    .requireAudience(aud)
                    .requireIssuer(iss)
                    .build()
                    .parseSignedClaims(idToken)
                    .getPayload();
             */

        } catch (UnsupportedJwtException e) {
            throw new UnsupportedJwtException("지원되지 않는 JWT 타입입니다");
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("비어있는 JWT 입니다");
        } catch (ExpiredJwtException e) {
            throw new RuntimeException("ID 토큰이 만료됐습니다");
        } catch (JwtException e) {
            throw new JwtException("JWT 검증 또는 분석 오류입니다");
        }
    }

    @Deprecated
    private String parsePayload(final String idToken) {
        final String encodedPayload = idToken.split(ID_TOKEN_VALUE_DELIMITER)[PAYLOAD_INDEX];
        final String decodedPayload = new String(Base64.getUrlDecoder().decode(encodedPayload));
        return decodedPayload;
    }
}
