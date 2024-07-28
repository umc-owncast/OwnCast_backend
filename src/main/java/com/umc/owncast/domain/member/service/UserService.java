package com.umc.owncast.domain.member.service;

import com.umc.owncast.common.exception.handler.UserHandler;
import com.umc.owncast.common.response.status.ErrorCode;
import com.umc.owncast.domain.language.repository.LanguageRepository;
import com.umc.owncast.domain.member.dto.MemberProfileRequestDTO;
import com.umc.owncast.domain.member.entity.Member;
import com.umc.owncast.domain.member.repository.MemberRepository;
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


}
