package com.umc.owncast.domain.cast.service;

import com.umc.owncast.domain.cast.dto.CastCreationRequestDTO;
import com.umc.owncast.domain.cast.dto.TTSResultDTO;
import com.umc.owncast.domain.sentence.service.SentenceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CastService {
    private final ScriptService scriptService;
    private final TranslateService translateService;
    private final TTSService ttsService;
    private final SentenceService sentenceService;

    public void createCast(CastCreationRequestDTO castRequest){
        //cast 저장 후

        String script = scriptService.createScript(castRequest);
        String korean = translateService.translate(script);
        TTSResultDTO ttsResultDTO = ttsService.createSpeech(script, castRequest);

        //cast를 parameter로 추가해서 보내야됨
        sentenceService.save(script,korean,ttsResultDTO);
    }
}
