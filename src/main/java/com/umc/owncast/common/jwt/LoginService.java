package com.umc.owncast.common.jwt;


import com.umc.owncast.common.exception.GeneralException;
import com.umc.owncast.common.response.status.ErrorCode;
import com.umc.owncast.domain.member.entity.Refresh;
import com.umc.owncast.domain.member.repository.RefreshRepository;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoginService {

    private final JwtUtil jwtUtil;
    private final RefreshRepository refreshRepository;


    @Value("${jwt.access.expire}")
    private Long accessExpirationTime;

    @Value("${jwt.refresh.expire}")
    private Long refreshExpirationTime;

    public String issueAccessToken(Long userId) {
        String accessToken = jwtUtil.createJwt("access", userId, accessExpirationTime*1000L);
        return "Bearer " + accessToken;
    }

    @Transactional
    public String issueRefreshToken(Long userId) {
        String refreshToken = jwtUtil.createJwt("refresh", userId, refreshExpirationTime*1000L);
        saveRefreshToken(userId, refreshToken, refreshExpirationTime);
        return refreshToken;
    }


    @Transactional
    public String reissueRefreshToken(Long userId, String refreshToken) {
        refreshRepository.deleteByRefreshToken(refreshToken);
        String newRefreshToken = jwtUtil.createJwt("refresh", userId, refreshExpirationTime*1000L);
        saveRefreshToken(userId, newRefreshToken, refreshExpirationTime);
        return newRefreshToken;
    }

    @Transactional
    public void revokeRefreshToken(String refreshToken) {
        refreshRepository.deleteByRefreshToken(refreshToken);
    }

    public String validateRefreshToken(Cookie[] cookies) {
        String refreshToken = null;
        for (Cookie cookie : cookies)
            if (cookie.getName().equals("refresh"))
                refreshToken = cookie.getValue();

        if (refreshToken == null)
            throw new GeneralException(ErrorCode.NOT_FOUND_TOKEN);

        try {
            jwtUtil.isExpired(refreshToken);
        } catch (ExpiredJwtException e) {
            throw new GeneralException(ErrorCode.EXPIRED_TOKEN);
        }

        String category = jwtUtil.getCategory(refreshToken);
        if (!category.equals("refresh"))
            throw new GeneralException(ErrorCode.INVALID_TOKEN);

        Boolean isExist = refreshRepository.existsByRefreshToken(refreshToken);
        if (!isExist)
            throw new GeneralException(ErrorCode.INVALID_TOKEN);

        return refreshToken;
    }

    private void saveRefreshToken(Long userId, String refreshToken, Long expirationTime) {
        LocalDateTime date = LocalDateTime.now(ZoneId.systemDefault()).plusSeconds(expirationTime);
        Refresh newRefresh = Refresh.builder()
                .userId(userId)
                .refreshToken(refreshToken)
                .expiredAt(date)
                .build();

        refreshRepository.save(newRefresh);
    }
}
