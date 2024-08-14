package com.umc.owncast.domain.cast.service;

import com.umc.owncast.domain.cast.dto.KeywordCastCreationDTO;
import org.springframework.stereotype.Service;

@Service
public interface ScriptService {
    String createScript(KeywordCastCreationDTO request);
}
