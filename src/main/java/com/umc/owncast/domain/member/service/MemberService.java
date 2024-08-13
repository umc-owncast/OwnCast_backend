package com.umc.owncast.domain.member.service;

import com.umc.owncast.common.exception.handler.UserHandler;
import com.umc.owncast.common.jwt.JwtUtil;
import com.umc.owncast.common.jwt.LoginService;
import com.umc.owncast.common.response.status.ErrorCode;
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
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final LanguageRepository languageRepository;
    private final MainPreferRepository mainPreferRepository;
    private final SubPreferRepository subPreferRepository;
    private final MainCategoryRepository mainCategoryRepository;
    private final SubCategoryRepository subCategoryRepository;


    @Transactional
    public String insertMember(HttpServletResponse response, MemberRequest.joinLoginIdDto requestDto) {
        if (memberRepository.existsByNickname(requestDto.getNickname())) {
            throw new UserHandler(ErrorCode.NICKNAME_ALREADY_EXIST); // 사용자 정의 예외
        }
        if (memberRepository.existsByLoginId(requestDto.getLoginId())) {
            throw new UserHandler(ErrorCode.ID_ALREADY_EXIST); // 사용자 정의 예외
        }

        Member newMember = MemberMapper.toLoginIdMember(requestDto.getLoginId(), bCryptPasswordEncoder.encode(requestDto.getPassword()), requestDto.getNickname(), requestDto.getUsername());
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
    public Long addLanguage(Long languageId) {
        // 토큰을 이용해서 사용자 정보 받기 (현재는 1L로 설정)
        Optional<Member> optionalMember = memberRepository.findById(1002L);

        System.out.println(optionalMember);

        if (optionalMember.isEmpty()) {
            throw new UserHandler(ErrorCode.MEMBER_NOT_FOUND);
        }

        Member member = optionalMember.get();

        // Language 엔티티를 안전하게 조회
        Language language = languageRepository.findById(languageId)
                .orElseThrow(() -> new UserHandler(ErrorCode.LANGUAGE_NOT_FOUND));

        // 언어 설정
        member.setLanguage(language);

        // 변경된 엔티티를 저장
        memberRepository.saveAndFlush(member);

        return member.getId();
    }


    @Transactional
    public Long addPreferSetting(Long memberId, MemberRequest.memberPreferDto request) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new UserHandler(ErrorCode.MEMBER_NOT_FOUND));

        MainPrefer mainPrefer = mainPreferRepository.findById(request.getMainCategoryId())
                .orElseThrow(() -> new UserHandler(ErrorCode.CATEGORY_NOT_EXIST));

        SubPrefer subPrefer;

        if (request.getSubCategoryId() != null) {
            subPrefer = subPreferRepository.findById(request.getSubCategoryId())
                    .orElseThrow(() -> new UserHandler(ErrorCode.SUBCATEGORY_NOT_EXIST));
        } else {
            if (request.getEtc() == null || request.getEtc().isEmpty()) {
                throw new UserHandler(ErrorCode.SUBCATEGORY_ETC_REQUIRED);
            }
            SubCategory newSubCategory = SubCategory.builder()
                    .name(request.getEtc())
                    .mainCategory(mainPrefer.getMainCategory())
                    .isUserCreated(true)
                    .build();
            subCategoryRepository.save(newSubCategory);

            subPrefer = SubPrefer.builder()
                    .member(member)
                    .subCategory(newSubCategory)
                    .build();
        }

        // Save new MainPrefer if needed
        MainPrefer newMainPrefer = MainPrefer.builder()
                .member(member)
                .mainCategory(mainPrefer.getMainCategory())
                .build();
        mainPreferRepository.save(newMainPrefer);

        subPreferRepository.save(subPrefer);

        return member.getId();
    }


}
