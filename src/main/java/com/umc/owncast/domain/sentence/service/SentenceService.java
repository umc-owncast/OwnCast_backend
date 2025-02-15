package com.umc.owncast.domain.sentence.service;

import com.umc.owncast.domain.cast.dto.TTSResultDTO;
import com.umc.owncast.domain.cast.entity.Cast;
import com.umc.owncast.domain.sentence.entity.Sentence;

import java.util.List;

public interface SentenceService {

    List<Sentence> saveSentences(String original, String[] originalList, TTSResultDTO ttsResultDTO, Cast cast);

    List<Sentence> saveSentences(String[] parsedOriginalScript, String[] parsedKoreanScript, TTSResultDTO ttsResultDTO, Cast cast);

    Sentence findById(Long sentenceId);

    void deleteAllByCast(Cast cast);
}
