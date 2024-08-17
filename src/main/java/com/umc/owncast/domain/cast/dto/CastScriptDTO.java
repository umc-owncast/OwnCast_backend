package com.umc.owncast.domain.cast.dto;

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
    private String fileUrl;
    private List<SentenceResponseDTO> sentences;
}
