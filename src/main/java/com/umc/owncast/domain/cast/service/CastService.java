package com.umc.owncast.domain.cast.service;


import com.umc.owncast.domain.cast.dto.CastCreationRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CastService {
    private final ScriptService scriptService;
    private final TranslateService translateService;
    private final TTSService ttsService;

    public String createCast(CastCreationRequestDTO castRequest){
        String script = scriptService.createScript(castRequest);
        translateService.translate(script);
        return ttsService.createSpeech(script, castRequest);
    }
}
