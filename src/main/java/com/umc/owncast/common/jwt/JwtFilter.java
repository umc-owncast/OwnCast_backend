package com.umc.owncast.common.jwt;

import com.umc.owncast.common.exception.GeneralException;
import com.umc.owncast.common.response.status.ErrorCode;
import com.umc.owncast.domain.member.dto.CustomUserDetails;
import com.umc.owncast.domain.member.entity.Member;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        try {
            String authorization = request.getHeader("Authorization");
            if (authorization == null || !authorization.startsWith("Bearer "))
                throw new GeneralException(ErrorCode.NOT_FOUND_TOKEN);

            String accessToken = authorization.split(" ")[1];
            jwtUtil.isExpired(accessToken);

            String category = jwtUtil.getCategory(accessToken);
            if (!category.equals("access"))
                throw new GeneralException((ErrorCode.INVALID_TOKEN));

            Long userId = jwtUtil.getUserId(accessToken);
            Member tempMember = Member.builder()
                    .id(userId)
                    .build();
            CustomUserDetails customUserDetails = new CustomUserDetails(tempMember);

            Authentication authToken = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authToken);
        } catch (ExpiredJwtException e) {
            request.setAttribute("exception", new GeneralException(ErrorCode.EXPIRED_TOKEN));
        } catch (GeneralException e) {
            request.setAttribute("exception", e);
        } catch (Exception e) {
            request.setAttribute("exception", new GeneralException(ErrorCode.INVALID_TOKEN));
        }

        filterChain.doFilter(request, response);
    }
}
