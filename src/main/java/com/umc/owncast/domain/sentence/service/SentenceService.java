package com.umc.owncast.domain.sentence.service;

import com.umc.owncast.domain.cast.dto.TTSResultDTO;

public interface SentenceService {

    //1차적으로 Cast 생성 된 후 cast를 추가하려 sentence 객체 생성하도록 수정
    void save(String original, String korean, TTSResultDTO ttsResultDTO);
}
