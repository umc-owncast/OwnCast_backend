package com.umc.owncast.domain.cast.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class CastHomeDTO {
    Long id;
    String audioLength;
    String title;
    String memberName;
    String playlistName;
}
