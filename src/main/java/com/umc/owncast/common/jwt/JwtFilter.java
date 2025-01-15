package com.umc.owncast.common.jwt;

import com.umc.owncast.common.exception.GeneralException;
import com.umc.owncast.common.exception.handler.UserHandler;
import com.umc.owncast.common.response.status.ErrorCode;
import com.umc.owncast.domain.member.dto.CustomUserDetails;
import com.umc.owncast.domain.member.entity.Member;
import com.umc.owncast.domain.member.repository.MemberRepository;
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
import java.util.NoSuchElementException;


@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final MemberRepository memberRepository;

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
            Member member = memberRepository.findById(jwtUtil.getUserId(accessToken))
                    .orElseThrow(()-> new NoSuchElementException("존재하지 않는 사용자입니다"));

            CustomUserDetails customUserDetails = new CustomUserDetails(member);
            Authentication authToken = new UsernamePasswordAuthenticationToken(customUserDetails,
                                                                                null,
                                                                                customUserDetails.getAuthorities());
            System.out.println("JwtFilter: request from member '" + member.getUsername() + "'");
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
