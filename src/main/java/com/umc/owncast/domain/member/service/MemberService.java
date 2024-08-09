package com.umc.owncast.domain.member.service;

import com.umc.owncast.common.exception.GeneralException;
import com.umc.owncast.common.jwt.JwtUtil;
import com.umc.owncast.common.jwt.LoginService;
import com.umc.owncast.common.response.status.ErrorCode;
import com.umc.owncast.domain.member.dto.MemberRequest;
import com.umc.owncast.domain.member.entity.Member;
import com.umc.owncast.domain.member.repository.MemberRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final LoginService loginService;
    private final JwtUtil jwtUtil;
    private final BCryptPasswordEncoder encoder;


    @Transactional
    public String insertMember(HttpServletResponse response, MemberRequest.joinLoginIdDto requestDto) {
        Member newMember = MemberMapper.toLoginIdMember(requestDto.getLoginId(), encoder.encode(requestDto.getPassword()), requestDto.getNickname(), requestDto.getUsername());
        Member savedMember = memberRepository.save(newMember);

        return issueToken(savedMember.getId(), response);
    }


    @Transactional
    public String reissueToken(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = loginService.validateRefreshToken(request.getCookies());

        Long userId = jwtUtil.getUserId(refreshToken);
        String newAccessToken = loginService.issueAccessToken(userId);
        String newRefreshToken = loginService.reissueRefreshToken(userId, refreshToken);

        response.addHeader("Authorization", newAccessToken);
        return newRefreshToken;
    }

    private String issueToken(Long memberId, HttpServletResponse response) {
        String newAccessToken = loginService.issueAccessToken(memberId);
        String newRefreshToken = loginService.issueRefreshToken(memberId);
        response.addHeader("Authorization", newAccessToken);
        return newRefreshToken;
    }

    @Transactional
    public void reactivate(MemberRequest.loginDto request) {
        Member selectedMember = memberRepository.findByLoginId(request.getLoginId())
                .orElseThrow(() -> new GeneralException(ErrorCode.MEMBER_NOT_FOUND));
        selectedMember.reactivate();
    }
}
