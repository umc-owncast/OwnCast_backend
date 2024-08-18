package com.umc.owncast.domain.member.service;

import com.umc.owncast.common.exception.handler.UserHandler;
import com.umc.owncast.common.jwt.JwtUtil;
import com.umc.owncast.common.jwt.LoginService;
import com.umc.owncast.common.jwt.SecurityUtils;
import com.umc.owncast.common.response.status.ErrorCode;
import com.umc.owncast.domain.category.entity.MainCategory;
import com.umc.owncast.domain.category.entity.SubCategory;
import com.umc.owncast.domain.category.repository.MainCategoryRepository;
import com.umc.owncast.domain.category.repository.SubCategoryRepository;
import com.umc.owncast.domain.enums.Language;
import com.umc.owncast.domain.member.dto.*;
import com.umc.owncast.domain.member.dto.MemberRequest.joinLoginIdDto;
import com.umc.owncast.domain.member.entity.Member;
import com.umc.owncast.domain.member.repository.MemberRepository;
import com.umc.owncast.domain.memberprefer.entity.MainPrefer;
import com.umc.owncast.domain.memberprefer.entity.SubPrefer;
import com.umc.owncast.domain.memberprefer.repository.MainPreferRepository;
import com.umc.owncast.domain.memberprefer.repository.SubPreferRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final LoginService loginService;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final MainPreferRepository mainPreferRepository;
    private final SubPreferRepository subPreferRepository;
    private final MainCategoryRepository mainCategoryRepository;
    private final SubCategoryRepository subCategoryRepository;


    @Transactional
    public RefreshTokenDto insertMember(HttpServletResponse response, joinLoginIdDto requestDto) {

        if (memberRepository.existsByNickname(requestDto.getNickname())) {
            throw new UserHandler(ErrorCode.NICKNAME_ALREADY_EXIST);
        }

        if (memberRepository.existsByLoginId(requestDto.getLoginId())) {
            throw new UserHandler(ErrorCode.ID_ALREADY_EXIST);
        }

        Member newMember = MemberMapper.toLoginIdMember(requestDto.getLoginId(), passwordEncoder.encode(requestDto.getPassword()), requestDto.getNickname(), requestDto.getUsername());
        Member savedMember = memberRepository.save(newMember);
        languageSetting(savedMember, requestDto.getLanguage());
        preferSetting(savedMember, MemberPreferRequestDTO.builder()
                .mainCategory(requestDto.getMainCategory())
                .subCategory(requestDto.getSubCategory())
                .build());

        return RefreshTokenDto.builder()
                .memberId(savedMember.getId())
                .refreshToken(issueToken(savedMember.getId(), response))
                .build();
    }

    public Boolean nickNameDuplicate(String nickName) {
        return memberRepository.existsByNickname(nickName);
    }

    public Boolean memberIdDuplicate(String loginId) {
        return memberRepository.existsByLoginId(loginId);
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

    public MemberSettingResponseDTO languageSetting(Member member, String language) {
        language = language.toUpperCase(Locale.ROOT);
        member.setLanguage(Language.valueOf(language));
        memberRepository.save(member);

        return MemberSettingResponseDTO.builder()
                .memberId(member.getId())
                .build();
    }

    public MemberSettingResponseDTO preferSetting(Member member, MemberPreferRequestDTO memberPreferRequestDTO) {
        MainCategory mainCategory = mainCategoryRepository.findByName(memberPreferRequestDTO.getMainCategory())
                .orElseThrow(() -> new UserHandler(ErrorCode.CATEGORY_NOT_EXIST));

        SubCategory subCategory = subCategoryRepository.findByName(memberPreferRequestDTO.getSubCategory())
                .orElseGet(() -> {
                    if (memberPreferRequestDTO.getSubCategory() == null) {
                        throw new UserHandler(ErrorCode.SUBCATEGORY_ETC_REQUIRED);
                    }
                    SubCategory newSubCategory =  SubCategory.builder()
                            .name(memberPreferRequestDTO.getSubCategory())
                            .mainCategory(mainCategory)
                            .isUserCreated(true)
                            .build();
                    subCategoryRepository.save(newSubCategory);
                    return newSubCategory;
                });

        Optional<MainPrefer> mainPrefer = mainPreferRepository.findByMember(member);
        if(mainPrefer.isPresent()) {
            mainPrefer.get().setMainCategory(mainCategory);
            mainPreferRepository.save(mainPrefer.get());
        } else {
            MainPrefer newMainPrefer = MainPrefer.builder()
                    .member(member)
                    .mainCategory(mainCategory)
                    .build();
            mainPreferRepository.save(newMainPrefer);
        }

        Optional<SubPrefer> subPrefer = subPreferRepository.findByMember(member);
        if(subPrefer.isPresent()) {
            subPrefer.get().setSubCategory(subCategory);
            subPreferRepository.save(subPrefer.get());
        } else {
            SubPrefer newSubPrefer = SubPrefer.builder()
                    .member(member)
                    .subCategory(subCategory)
                    .build();
            subPreferRepository.save(newSubPrefer);
        }

        return MemberSettingResponseDTO.builder()
                .memberId(1L)
                .build();
    }

    public MemberSettingResponseDTO profileSetting(MemberProfileRequestDTO memberProfileRequestDTO) {


        Member member = memberRepository.findById(1L).orElseThrow(() -> new UserHandler(ErrorCode.MEMBER_NOT_FOUND));

        if (memberRepository.existsByNickname(memberProfileRequestDTO.getNickname())) {
            throw new UserHandler(ErrorCode.NICKNAME_ALREADY_EXIST);
        }

        if (memberRepository.existsByLoginId(memberProfileRequestDTO.getLoginId())) {
            throw new UserHandler(ErrorCode.ID_ALREADY_EXIST);
        }

        member.setMember(
                memberProfileRequestDTO.getLoginId(),
                memberProfileRequestDTO.getUsername(),
                memberProfileRequestDTO.getNickname());
        memberRepository.save(member);

        return MemberSettingResponseDTO.builder()
                .memberId(1L)
                .build();
    }

    public MemberSettingResponseDTO passwordSetting(MemberPasswordRequestDTO memberPasswordRequestDTO) {
        Member member = memberRepository.findById(1L).orElseThrow(() -> new UserHandler(ErrorCode.MEMBER_NOT_FOUND));

        member.setPassword(passwordEncoder.encode(memberPasswordRequestDTO.getPassword()));
        memberRepository.save(member);

        return MemberSettingResponseDTO.builder()
                .memberId(1L)
                .build();
    }

    public Member getCurrentMemberName() {

        String userEmail = SecurityUtils.getCurrentUsername().orElseThrow(() -> new UserHandler(ErrorCode._UNAUTHORIZED));

        return memberRepository.findByUsername(userEmail)
                .orElseThrow(() -> new UserHandler(ErrorCode.MEMBER_NOT_FOUND));
    }
}
