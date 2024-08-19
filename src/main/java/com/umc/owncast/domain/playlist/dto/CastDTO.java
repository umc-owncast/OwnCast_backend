package com.umc.owncast.domain.playlist.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Setter
@Getter
public class CastDTO {
    Long castId;
    Long playlistId;
    String castTitle;
    Boolean isPublic;
    String castCreator;
    String castCategory;
    String audioLength;
}
