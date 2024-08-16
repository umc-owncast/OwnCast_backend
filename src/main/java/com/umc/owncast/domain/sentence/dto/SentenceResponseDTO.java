package com.umc.owncast.domain.sentence.dto;

import com.umc.owncast.domain.sentence.entity.Sentence;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SentenceResponseDTO {
    private Long id;
    private String originalSentence;
    private String translatedSentence;
    private Double timePoint;

    public SentenceResponseDTO(Sentence sentence) {
        id = sentence.getId();
        originalSentence = sentence.getOriginalSentence();
        translatedSentence = sentence.getTranslatedSentence();
        timePoint = sentence.getTimePoint();
    }
}
