package com.umc.owncast.domain.member.service;

import com.umc.owncast.common.exception.handler.UserHandler;
import com.umc.owncast.common.jwt.JwtAuthenticationFilter;
import com.umc.owncast.common.jwt.LoginService;
import com.umc.owncast.common.jwt.TokenProvider;
import com.umc.owncast.common.response.status.ErrorCode;
import com.umc.owncast.domain.category.entity.SubCategory;
import com.umc.owncast.domain.category.repository.SubCategoryRepository;
import com.umc.owncast.domain.enums.Language;
import com.umc.owncast.domain.enums.MainCategory;
import com.umc.owncast.domain.member.dto.*;
import com.umc.owncast.domain.member.dto.MemberRequest.JoinLoginIdDto;
import com.umc.owncast.domain.member.entity.Member;
import com.umc.owncast.domain.member.repository.MemberRepository;
import com.umc.owncast.domain.memberprefer.entity.SubPrefer;
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
    private final PasswordEncoder passwordEncoder;
    private final SubPreferRepository subPreferRepository;
    private final SubCategoryRepository subCategoryRepository;
    private final TokenProvider tokenProvider;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;


    @Transactional
    public RefreshTokenDto insertMember(HttpServletResponse response, JoinLoginIdDto requestDto) {

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
                .refreshToken(issueToken(savedMember.getUsername(), response))
                .build();
    }

    public Boolean nickNameDuplicate(String nickName) {
        return memberRepository.existsByNickname(nickName);
    }

    public Boolean memberIdDuplicate(String loginId) {
        return memberRepository.existsByLoginId(loginId);
    }

    public MemberInfoDTO getMemberInfo(Member member){
        Optional<SubPrefer> subPrefer = subPreferRepository.findByMember(member);
        if(subPrefer.isEmpty()){
            throw new UserHandler(ErrorCode.SUBCATEGORY_NOT_EXIST);
        }
        return MemberInfoDTO.builder()
                .loginId(member.getLoginId())
                .language(member.getLanguage())
                .nickname(member.getNickname())
                .username(member.getUsername())
                .mainCategory(subPrefer.get().getSubCategory().getMainCategory().getKrSubCategory())
                .subCategory(subPrefer.get().getSubCategory().getName())
                .build();
    }

    @Transactional
    public String reissueToken(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = jwtAuthenticationFilter.resolveToken(request);

        String username  = tokenProvider.getAuthentication(refreshToken).getName();
        String newAccessToken = loginService.issueAccessToken(username);
        String newRefreshToken = loginService.reissueRefreshToken(username, refreshToken);

        response.addHeader("Authorization", newAccessToken);
        return newRefreshToken;
    }

    private String issueToken(String username, HttpServletResponse response) {
        String newAccessToken = loginService.issueAccessToken(username);
        String newRefreshToken = loginService.issueRefreshToken(username);
        response.addHeader("Authorization", newAccessToken);
        return newRefreshToken;
    }

    public MemberSettingResponseDTO languageSetting(Member member, String language) {
        language = language.toUpperCase(Locale.ROOT).trim();
        member.setLanguage(Language.valueOf(language));
        memberRepository.save(member);

        return MemberSettingResponseDTO.builder()
                .memberId(member.getId())
                .build();
    }

    public MemberSettingResponseDTO preferSetting(Member member, MemberPreferRequestDTO memberPreferRequestDTO) {
        MainCategory mainCategory = MainCategory.fromValue(memberPreferRequestDTO.getMainCategory());
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
                .memberId(member.getId())
                .build();
    }

    public MemberSettingResponseDTO profileSetting(Member member, MemberProfileRequestDTO memberProfileRequestDTO) {
        if (memberRepository.existsByNickname(memberProfileRequestDTO.getNickname())) {
            if(!member.getNickname().equals(memberProfileRequestDTO.getNickname())) {
                throw new UserHandler(ErrorCode.NICKNAME_ALREADY_EXIST);
            }
        }

        if (memberRepository.existsByLoginId(memberProfileRequestDTO.getLoginId())) {
            if(!member.getLoginId().equals(memberProfileRequestDTO.getLoginId())) {
                throw new UserHandler(ErrorCode.ID_ALREADY_EXIST);
            }
        }

        member.updateMember(
                memberProfileRequestDTO.getLoginId(),
                memberProfileRequestDTO.getUsername(),
                memberProfileRequestDTO.getNickname());
        memberRepository.save(member);

        return MemberSettingResponseDTO.builder()
                .memberId(member.getId())
                .build();
    }

    public MemberSettingResponseDTO passwordSetting(Member member, MemberPasswordRequestDTO memberPasswordRequestDTO) {
        member.setPassword(passwordEncoder.encode(memberPasswordRequestDTO.getPassword()));
        memberRepository.save(member);

        return MemberSettingResponseDTO.builder()
                .memberId(member.getId())
                .build();
    }
}
