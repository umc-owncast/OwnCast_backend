package com.umc.owncast.domain.cast.service;


import com.umc.owncast.domain.cast.dto.CastCreationRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CastService {
    private final ScriptService scriptService;
    private final TTSService ttsService;

    public String createCast(CastCreationRequestDTO castRequest){
        String script = scriptService.createScript(castRequest);
        //번역 추후 구현
        return ttsService.createSpeech(script,castRequest);
    }
}
