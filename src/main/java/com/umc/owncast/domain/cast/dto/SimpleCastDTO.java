package com.umc.owncast.domain.cast.dto;

import com.umc.owncast.domain.cast.entity.Cast;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SimpleCastDTO {
    private Long id;
    private String title;
    private String imagePath;
    private String audioLength;
    private Boolean isPublic;
    private Long hits;

    public SimpleCastDTO(Cast cast){
        id = cast.getId();
        title = cast.getTitle();
        imagePath = cast.getImagePath();
        audioLength = cast.getAudioLength();
        isPublic = cast.getIsPublic();
        hits = cast.getHits();
    }
}
