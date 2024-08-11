package com.umc.owncast.domain.playlist.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

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

    @Setter
    @Getter
    @Builder
    public static class PlaylistResultDTO {
        String name;
        String imagePath;
        Long playlistId;
        Long totalCast;
    }

    @Builder
    @Setter
    @Getter
    public static class DeletePlaylistDTO {
        Long playlistId;
        Long totalCast;
    }

    @Builder
    @Setter
    @Getter
    public static class GetPlaylistDTO {
        List<CastDTO> castList;
    }

    @Builder
    @Setter
    @Getter
    public static class CastDTO {
        Long castId;
        String castTitle;
        Boolean isPublic;
        String castCreator;
        String castCategory;
        String audioLength;
    }

}
