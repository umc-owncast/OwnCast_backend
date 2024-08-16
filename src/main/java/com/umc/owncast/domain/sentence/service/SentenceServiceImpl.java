package com.umc.owncast.domain.sentence.service;

import com.umc.owncast.common.exception.handler.UserHandler;
import com.umc.owncast.common.response.status.ErrorCode;
import com.umc.owncast.domain.cast.dto.TTSResultDTO;
import com.umc.owncast.domain.cast.entity.Cast;
import com.umc.owncast.domain.cast.service.ParsingService;
import com.umc.owncast.domain.sentence.entity.Sentence;
import com.umc.owncast.domain.sentence.repository.SentenceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SentenceServiceImpl implements SentenceService {

    private final TranslateService translateService;
    private final SentenceRepository sentenceRepository;
    private final ParsingService parsingService;

    @Override
    public List<Sentence> save(String original, TTSResultDTO ttsResultDTO, Cast cast) {
        int i = 0;
        String koreanScript = translateService.translate(original);
        String[] originalList = parsingService.parseSentences(original);
        String[] koreanList = parsingService.parseSentences(koreanScript);
        List<Sentence> sentences = new ArrayList<>();
        for (Double timepoint : ttsResultDTO.getTimePointList()) {
            Sentence sentence = Sentence.builder()
                    .cast(cast)
                    .originalSentence(originalList[i])
                    .translatedSentence(koreanList[i])
                    .timePoint(timepoint)
                    .build();
            i++;
            Sentence savedSentence = sentenceRepository.save(sentence);
            sentences.add(savedSentence);
        }
        return sentences;
    }

    @Override
    public List<Sentence> findCastSentence(Long castId) {
        return sentenceRepository.findAllByCastIdOrderByTimePointAsc(castId);
    }

    @Override
    public Sentence findById(Long sentenceId) {
        return sentenceRepository.findById(sentenceId)
                .orElseThrow(() -> new UserHandler(ErrorCode.SENTENCE_NOT_FOUND));
    }

}
