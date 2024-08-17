package com.umc.owncast.domain.cast.service.chatGPT.script;

import com.umc.owncast.domain.cast.dto.KeywordCastCreationDTO;

public interface ScriptService {
    String createScript(KeywordCastCreationDTO request);
}
