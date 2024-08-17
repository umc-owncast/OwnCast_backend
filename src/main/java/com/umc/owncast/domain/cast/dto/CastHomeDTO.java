package com.umc.owncast.domain.cast.dto;

import com.umc.owncast.domain.cast.entity.Cast;
import com.umc.owncast.domain.sentence.dto.SentenceResponseDTO;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class CastHomeDTO {
    Long id;
    String title;
    String imagePath;
    String audioLength;
    String memberName;
    String playlistName;
}
