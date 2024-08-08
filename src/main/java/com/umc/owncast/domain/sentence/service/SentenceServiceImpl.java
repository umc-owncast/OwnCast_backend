package com.umc.owncast.domain.sentence.service;

import com.umc.owncast.domain.cast.dto.TTSResultDTO;
import com.umc.owncast.domain.cast.service.ParsingService;
import com.umc.owncast.domain.sentence.entity.Sentence;
import com.umc.owncast.domain.sentence.repository.SentenceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SentenceServiceImpl implements SentenceService {

    private final SentenceRepository sentenceRepository;
    private final ParsingService parsingService;

    @Override
    public void save(String original, String korean, TTSResultDTO ttsResultDTO) {
        int i = 0;
        String[] originalList = parsingService.parseSentences(original);
        String[] koreanList = parsingService.parseSentences(korean);
        for(Double timepoint : ttsResultDTO.getTimePointList()) {
            Sentence sentence = Sentence.builder()
                    .originalSentence(originalList[i])
                    .translatedSentence(koreanList[i])
                    .timePoint(timepoint)
                    .build();
            i++;
            sentenceRepository.save(sentence);
        }
    }
}
