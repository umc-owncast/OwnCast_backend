package com.umc.owncast.domain.cast.dto;

import com.umc.owncast.domain.cast.entity.Cast;
import com.umc.owncast.domain.sentence.dto.SentenceResponseDTO;
import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CastScriptDTO {
    private Long id;
    private List<SentenceResponseDTO> sentences;

    public CastScriptDTO(Cast cast){
        id = cast.getId();
        sentences = cast.getSentences().stream()
                .map(SentenceResponseDTO::new)
                .toList();
    }
}
