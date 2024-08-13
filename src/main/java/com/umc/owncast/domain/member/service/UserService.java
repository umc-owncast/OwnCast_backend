package com.umc.owncast.domain.member.service;

import com.umc.owncast.common.exception.handler.UserHandler;
import com.umc.owncast.common.response.status.ErrorCode;
import com.umc.owncast.domain.category.entity.MainCategory;
import com.umc.owncast.domain.category.entity.SubCategory;
import com.umc.owncast.domain.category.repository.MainCategoryRepository;
import com.umc.owncast.domain.category.repository.SubCategoryRepository;
import com.umc.owncast.domain.language.entity.Language;
import com.umc.owncast.domain.language.repository.LanguageRepository;
import com.umc.owncast.domain.member.dto.MemberPreferRequestDTO;
import com.umc.owncast.domain.member.dto.MemberProfileRequestDTO;
import com.umc.owncast.domain.member.entity.Member;
import com.umc.owncast.domain.member.repository.MemberRepository;
import com.umc.owncast.domain.memberprefer.entity.MainPrefer;
import com.umc.owncast.domain.memberprefer.entity.SubPrefer;
import com.umc.owncast.domain.memberprefer.repository.MainPreferRepository;
import com.umc.owncast.domain.memberprefer.repository.SubPreferRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserService {

    private final MemberRepository memberRepository;
    private final LanguageRepository languageRepository;
    private final MainPreferRepository mainPreferRepository;
    private final SubPreferRepository subPreferRepository;
    private final MainCategoryRepository mainCategoryRepository;
    private final SubCategoryRepository subCategoryRepository;

    public Long languageSetting(Long languageId) {

        // 토큰을 이용해서 사용자 정보 받기
        // 일단은 1L로 설정

        Optional<Member> optionalMember = memberRepository.findById(1L);
        Member member;

        if (optionalMember.isEmpty()) {
            throw new UserHandler(ErrorCode.MEMBER_NOT_FOUND);
        } else {
            member = optionalMember.get();

            Optional<Language> optionalLanguage = languageRepository.findById(languageId);

            if (optionalLanguage.isEmpty()) {
                throw new UserHandler(ErrorCode.LANGUAGE_NOT_FOUND);
            }

            member.setLanguage(optionalLanguage.get());
            memberRepository.save(member);
        }

        return member.getId();
    }

    public Long preferSetting(MemberPreferRequestDTO memberPreferRequestDTO) {

        // 토큰을 이용해서 사용자 정보 받기
        // 일단은 1L로 설정

        Optional<Member> optionalMember = memberRepository.findById(1L);
        Optional<MainCategory> optionalMainCategory = mainCategoryRepository.findById(memberPreferRequestDTO.getMainCategoryId()); // 주 카테고리
        Optional<SubCategory> optionalSubCategory = subCategoryRepository.findById(memberPreferRequestDTO.getSubCategoryId()); // 부 카테고리 찾기

        if (optionalMember.isEmpty()) {
            throw new UserHandler(ErrorCode.MEMBER_NOT_FOUND);
        }

        if (optionalMainCategory.isEmpty() || (optionalSubCategory.isEmpty() && memberPreferRequestDTO.getEtc() == null)) {
            throw new UserHandler(ErrorCode.CATEGORY_NOT_EXIST);
        }

        Member member = optionalMember.get();
        MainCategory mainCategory = optionalMainCategory.get();

        Optional<MainPrefer> optionalMainPrefer = mainPreferRepository.findByMember(member);
        Optional<SubPrefer> optionalSubPrefer = subPreferRepository.findByMember(member);

        if (optionalMainPrefer.isEmpty() || optionalSubPrefer.isEmpty()) {
            throw new UserHandler(ErrorCode._BAD_REQUEST);
        }

        MainPrefer mainPrefer = optionalMainPrefer.get();
        SubPrefer subPrefer = optionalSubPrefer.get();
        SubCategory oldSubCategory = subPrefer.getSubCategory(); // 멤버의 기존 subCategory

        mainPrefer.setMainCategory(mainCategory);

        SubCategory subCategory;
        subCategory = optionalSubCategory.orElseGet(() -> SubCategory.builder() // 만약 부카테고리가 null이라면, etc에 값이 들어있음. 해당 값으로 새로운 부 카테고리 생성
                .name(memberPreferRequestDTO.getEtc())
                .mainCategory(mainCategory)
                .isUserCreated(true)
                .build());

        subPrefer.setSubCategory(subCategory);
        subCategoryRepository.save(subCategory);

        if (oldSubCategory.getIsUserCreated()) {
            subCategoryRepository.delete(oldSubCategory); // 기존 부 카테고리가 유저가 만든 카테고리라면 삭제하기
        }

        return member.getId();
    }

    public Long idPasswordSetting(MemberProfileRequestDTO memberProfileRequestDTO) {

        // 토큰을 이용해서 사용자 정보 받기
        // 일단은 1L로 설정

        Optional<Member> optionalMember = memberRepository.findById(1L);
        Member member;

        if (optionalMember.isEmpty()) {
            throw new UserHandler(ErrorCode.MEMBER_NOT_FOUND);
        } else {
            member = optionalMember.get();
            member.setMember(
                    memberProfileRequestDTO.getUsername(),
                    memberProfileRequestDTO.getPassword(),
                    memberProfileRequestDTO.getNickname());

            memberRepository.save(member);

        }
        return member.getId();
    }


}
