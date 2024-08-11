package com.umc.owncast.domain.playlist.dto;

import lombok.Builder;
import lombok.Getter;

public class PlaylistDTO {

    @Getter
    @Builder
    public static class PlaylistResultDTO {
        String name;
        String imagePath;
        Long playlistId;
        Long totalCast;
    }
}
