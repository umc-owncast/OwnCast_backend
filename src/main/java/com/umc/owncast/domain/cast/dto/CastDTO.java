package com.umc.owncast.domain.cast.dto;

import com.umc.owncast.domain.cast.entity.Cast;
import com.umc.owncast.domain.enums.Formality;
import com.umc.owncast.domain.sentence.dto.SentenceResponseDTO;
import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CastDTO {
    private Long id;
    private String title;
    private String imagePath;
    private String audioLength;
    private String fileUrl;
    private List<SentenceResponseDTO> sentences;

    public CastDTO(Cast cast) {
        id = cast.getId();
        title = cast.getTitle();
        imagePath = cast.getImagePath();
        audioLength = cast.getAudioLength();
        fileUrl = cast.getFilePath();
        sentences = cast.getSentences().stream()
                .map(SentenceResponseDTO::new)
                .toList();
    }
}
