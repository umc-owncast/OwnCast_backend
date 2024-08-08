package com.umc.owncast.domain.member.service;

import com.umc.owncast.common.exception.handler.UserHandler;
import com.umc.owncast.common.response.status.ErrorCode;
import com.umc.owncast.domain.category.entity.MainCategory;
import com.umc.owncast.domain.category.entity.SubCategory;
import com.umc.owncast.domain.category.main_category.repository.MainCategoryRepository;
import com.umc.owncast.domain.category.sub_category.repository.SubCategoryRepository;
import com.umc.owncast.domain.language.repository.LanguageRepository;
import com.umc.owncast.domain.member.dto.MemberPreferRequestDTO;
import com.umc.owncast.domain.member.dto.MemberProfileRequestDTO;
import com.umc.owncast.domain.member.entity.Member;
import com.umc.owncast.domain.member.repository.MemberRepository;
import com.umc.owncast.domain.memberprefer.entity.MainPrefer;
import com.umc.owncast.domain.memberprefer.entity.MainPreferRepository;
import com.umc.owncast.domain.memberprefer.entity.SubPrefer;
import com.umc.owncast.domain.memberprefer.entity.SubPreferRepository;
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

    public Long languageSetting(Long languageId){

        // 토큰을 이용해서 사용자 정보 받기
        // 일단은 1L로 설정

        Optional<Member> optionalMember = memberRepository.findById(1L);
        Member member;

        if(optionalMember.isEmpty()){
            throw new UserHandler(ErrorCode.MEMBER_NOT_FOUND);
        } else {
            member = optionalMember.get();
            member.changeLanguage(languageRepository.findById(languageId).get());
            memberRepository.save(member);
        }

        return member.getId();
    }

    public Long preferSetting(MemberPreferRequestDTO memberPreferRequestDTO){

        // 토큰을 이용해서 사용자 정보 받기
        // 일단은 1L로 설정

        Optional<Member> optionalMember = memberRepository.findById(1L);
        Optional<MainCategory> optionalMainCategory = mainCategoryRepository.findById(memberPreferRequestDTO.getMainCategoryId());
        Optional<SubCategory> optionalSubCategory = subCategoryRepository.findById(memberPreferRequestDTO.getSubCategoryId());

        if(optionalMember.isEmpty()){
            throw new UserHandler(ErrorCode.MEMBER_NOT_FOUND);
        }

        if(optionalMainCategory.isEmpty() || (optionalSubCategory.isEmpty() && memberPreferRequestDTO.getEtc() == null)){
            throw new UserHandler(ErrorCode.CATEGORY_NOT_EXIST);
        }

        Member member = optionalMember.get();
        MainCategory mainCategory = optionalMainCategory.get();

        Optional<MainPrefer> optionalMainPrefer = mainPreferRepository.findByMember(member);
        Optional<SubPrefer> optionalSubPrefer = subPreferRepository.findByMember(member);

        if(optionalMainPrefer.isEmpty() || optionalSubPrefer.isEmpty()){
            throw new UserHandler(ErrorCode._BAD_REQUEST);
        }

        MainPrefer mainPrefer = optionalMainPrefer.get();
        SubPrefer subPrefer = optionalSubPrefer.get();

        mainPrefer.setMainCategory(mainCategory);

        SubCategory subCategory;

        subCategory = optionalSubCategory.orElseGet(() -> SubCategory.builder()
                .name(memberPreferRequestDTO.getEtc())
                .mainCategory(mainCategory)
                .build());

        subPrefer.setSubCategory(subCategory);
        subCategoryRepository.save(subCategory);

        return member.getId();
    }


}
