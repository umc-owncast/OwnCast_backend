package com.umc.owncast.domain.sentence.service;

import com.umc.owncast.domain.cast.dto.TTSResultDTO;
import com.umc.owncast.domain.cast.entity.Cast;
import com.umc.owncast.domain.sentence.entity.Sentence;

import java.util.List;

public interface SentenceService {

    /** 1차적으로 Cast 생성 된 후 cast를 추가하려 sentence 객체 생성하도록 수정 */
    void save(String original, TTSResultDTO ttsResultDTO, Cast cast);

    /** 매개변수를 묶어 List<Sentence>로 반환 */
    List<Sentence> mapToSentence(String original, String korean, TTSResultDTO ttsResultDTO, Cast cast);

    List<Sentence> findCastSentence(Long castId);
}
