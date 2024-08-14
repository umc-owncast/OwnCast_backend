package com.umc.owncast.domain.cast.service.chatGPT.script;

import com.umc.owncast.domain.cast.dto.KeywordCastCreationDTO;
import org.springframework.stereotype.Service;

@Service
public interface ScriptService {
    String createScript(KeywordCastCreationDTO request);
}
