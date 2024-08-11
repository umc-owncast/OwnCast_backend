package com.umc.owncast.domain.sentence.service;

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
        for(Double timepoint : ttsResultDTO.getTimePointList()) {
            Sentence sentence = Sentence.builder()
                    .cast(cast)
                    .originalSentence(originalList[i])
                    .translatedSentence(koreanList[i])
                    .timePoint(timepoint)
                    .build();
            i++;
            sentences.add(sentence);
        }
        return sentenceRepository.saveAll(sentences);
    }

    @Override
    public List<Sentence> mapToSentence(String original, String korean, TTSResultDTO ttsResultDTO, Cast cast){
        // TODO 테스트 필요
        int i = 0;
        List<Sentence> sentences = new ArrayList<>();
        String[] originalList = parsingService.parseSentences(original);
        String[] koreanList = parsingService.parseSentences(korean);
        for(Double timepoint : ttsResultDTO.getTimePointList()) {
            sentences.add(Sentence.builder()
                    .cast(cast)
                    .originalSentence(originalList[i])
                    .translatedSentence(koreanList[i])
                    .timePoint(timepoint)
                    .build()
            );
            i++;
        }
        return sentences;
    }

    @Override
    public List<Sentence> findCastSentence(Long castId) {
        return sentenceRepository.findAllByCastIdOrderByTimePointAsc(castId);
    }
}
