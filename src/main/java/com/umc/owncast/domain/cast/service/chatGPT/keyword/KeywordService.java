package com.umc.owncast.domain.cast.service.chatGPT.keyword;

import com.umc.owncast.domain.member.entity.Member;

import java.util.List;

public interface KeywordService {
    List<String> createKeyword(Member member);
}
