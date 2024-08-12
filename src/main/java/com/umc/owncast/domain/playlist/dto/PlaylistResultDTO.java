package com.umc.owncast.domain.playlist.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Setter
@Getter
public class PlaylistResultDTO {
    String name;
    String imagePath;
    Long playlistId;
    Long totalCast;
}
