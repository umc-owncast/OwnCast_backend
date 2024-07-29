package com.umc.owncast.domain.playlist.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

public class PlaylistDTO {

    @Builder
    @Setter
    @Getter
    public static class AddPlaylistDTO {
        Long playlistId;
    }

    @Builder
    @Setter
    @Getter
    public static class ModifyPlaylistDTO {
        Long playlistId;
        String playlistName;
    }

    @Builder
    @Setter
    @Getter
    public static class DeletePlaylistDTO {
        Long playlistId;
    }
}
