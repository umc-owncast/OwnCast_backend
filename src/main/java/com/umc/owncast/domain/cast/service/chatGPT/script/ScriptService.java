package com.umc.owncast.domain.cast.service.chatGPT.script;

import com.umc.owncast.domain.cast.dto.KeywordCastCreationDTO;
import com.umc.owncast.domain.member.entity.Member;

public interface ScriptService {
    String createScript(Member member, KeywordCastCreationDTO request);
}
