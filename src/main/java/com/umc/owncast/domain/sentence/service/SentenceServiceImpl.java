package com.umc.owncast.domain.sentence.service;

import com.umc.owncast.common.annotation.TrackExecutionTime;
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

    private final TranslationService translationService;
    private final SentenceRepository sentenceRepository;
    private final ParsingService parsingService;

    @Override
    @TrackExecutionTime
    public List<Sentence> save(String original, String[] originalList, TTSResultDTO ttsResultDTO, Cast cast) {
        String koreanScript = translationService.translateToKorean(original);
        String[] koreanList = parsingService.parseSentencesByDelimiter(koreanScript);
        List<Sentence> sentences = new ArrayList<>();
        for (int i=0; i< ttsResultDTO.getTimePointList().size()-1; i++) {
            Sentence sentence = Sentence.builder()
                    .cast(cast)
                    .originalSentence(originalList[i])
                    .translatedSentence(koreanList[i])
                    .timePoint(ttsResultDTO.getTimePointList().get(i))
                    .build();
            Sentence savedSentence = sentenceRepository.save(sentence);
            sentences.add(savedSentence);
        }
        System.out.println();
        return sentences;
    }

    @Override
    public Sentence findById(Long sentenceId) {
        return sentenceRepository.findById(sentenceId)
                .orElseThrow(() -> new UserHandler(ErrorCode.SENTENCE_NOT_FOUND));
    }

    @Override
    public void deleteAllByCast(Cast cast) {
        List<Sentence> sentenceList = sentenceRepository.findAllByCastId(cast.getId());
        sentenceRepository.deleteAll(sentenceList);
    }

}
