package com.umc.owncast.domain.member.service;

import com.umc.owncast.common.exception.GeneralException;
import com.umc.owncast.common.exception.handler.UserHandler;
import com.umc.owncast.common.jwt.JwtUtil;
import com.umc.owncast.common.jwt.LoginService;
import com.umc.owncast.common.response.status.ErrorCode;
import com.umc.owncast.domain.category.entity.MainCategory;
import com.umc.owncast.domain.category.entity.SubCategory;
import com.umc.owncast.domain.category.repository.MainCategoryRepository;
import com.umc.owncast.domain.category.repository.SubCategoryRepository;
import com.umc.owncast.domain.language.entity.Language;
import com.umc.owncast.domain.language.repository.LanguageRepository;
import com.umc.owncast.domain.member.dto.MemberRequest;
import com.umc.owncast.domain.member.entity.Member;
import com.umc.owncast.domain.member.repository.MemberRepository;
import com.umc.owncast.domain.memberprefer.entity.MainPrefer;
import com.umc.owncast.domain.memberprefer.entity.SubPrefer;
import com.umc.owncast.domain.memberprefer.repository.MainPreferRepository;
import com.umc.owncast.domain.memberprefer.repository.SubPreferRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final LoginService loginService;
    private final JwtUtil jwtUtil;
    private final BCryptPasswordEncoder encoder;
    private final LanguageRepository languageRepository;
    private final MainPreferRepository mainPreferRepository;
    private final SubPreferRepository subPreferRepository;
    private final MainCategoryRepository mainCategoryRepository;
    private final SubCategoryRepository subCategoryRepository;


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
    public Long addLanguageSetting(Long memberId, Long languageId){

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new UserHandler(ErrorCode.MEMBER_NOT_FOUND));

        Language language = languageRepository.findById(languageId)
                .orElseThrow(() -> new UserHandler(ErrorCode.LANGUAGE_NOT_FOUND));

        member.getLanguage(language);
        memberRepository.save(member);

        return member.getId();
    }

    @Transactional
    public Long addPreferSetting(Long memberId, MemberRequest.memberPreferDto request) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new UserHandler(ErrorCode.MEMBER_NOT_FOUND));

        // MainCategory 조회
        MainCategory mainCategory = mainCategoryRepository.findById(request.getMainCategoryId())
                .orElseThrow(() -> new UserHandler(ErrorCode.CATEGORY_NOT_EXIST));

        // SubCategory 조회 또는 새로 생성
        SubCategory subCategory = subCategoryRepository.findById(request.getSubCategoryId())
                .orElseGet(() -> SubCategory.builder()
                        .name(request.getEtc()) // 사용자가 입력한 이름으로 SubCategory 생성
                        .mainCategory(mainCategory) // 관련된 MainCategory 설정
                        .isUserCreated(true) // 사용자가 만든 SubCategory임을 표시
                        .build());

        // 새로운 MainPrefer 생성 및 저장
        MainPrefer newMainPrefer = MainPrefer.builder()
                .member(member)
                .mainCategory(mainCategory)
                .build();
        mainPreferRepository.save(newMainPrefer);

        // 새로운 SubPrefer 생성 및 저장
        SubPrefer newSubPrefer = SubPrefer.builder()
                .member(member)
                .subCategory(subCategory)
                .build();
        subPreferRepository.save(newSubPrefer);

        // 사용자가 만든 새로운 SubCategory 저장
        if (subCategory.isUserCreated()) {
            subCategoryRepository.save(subCategory);
        }

        return member.getId();
    }

}
