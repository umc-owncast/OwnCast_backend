package com.umc.owncast.domain.playlist.factory;

import com.umc.owncast.domain.castplaylist.entity.CastPlaylist;
import com.umc.owncast.domain.playlist.dto.*;
import com.umc.owncast.domain.playlist.entity.Playlist;
import com.umc.owncast.domain.playlist.template.crud.UpdatePlaylist;
import org.springframework.beans.factory.annotation.Value;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PlaylistDTOFactory {

    public static PlaylistResultDTO createPlaylistResultDTO(String name, String imagePath, Long playlistId, Long totalCast){
        return PlaylistResultDTO.builder()
                .name(name)
                .imagePath(imagePath)
                .playlistId(playlistId)
                .totalCast(totalCast)
                .build();
    }

    public static List<PlaylistResultDTO> createMultiplePlaylistResultDTO(List<Playlist> playlists){

        // playlistDTOList 초기화
        List<PlaylistResultDTO> playlistDTOList = new ArrayList<>();

        playlists.forEach(playlist -> {
            String playlistName = playlist.getName();
            String playlistImagePath = playlist.getImagePath();
            Long playlistId = playlist.getId();
            Long totalCast = playlist.getCastSize();
            playlistDTOList.add(createPlaylistResultDTO(playlistName, playlistImagePath, playlistId, totalCast));
        });

        return playlistDTOList;
    }

    public static List<CastDTO> createCastDTOList(List<CastPlaylist> castPlaylist) {
        return castPlaylist.stream()
                .map(PlaylistDTOFactory::convertToCastDTO) // 변환 메서드 호출
                .collect(Collectors.toList());
    }

    public static CreatePlaylistDTO createCreatePlaylistDTO(Long playlistId){
        return CreatePlaylistDTO.builder()
                .playlistId(playlistId)
                .build();
    }

    public static DeletePlaylistDTO createDeletePlaylistDTO(Long playlistId){
        return DeletePlaylistDTO.builder()
                .playlistId(playlistId)
                .build();
    }

    public static UpdatePlaylistDTO createUpdatePlaylistDTO(Playlist playlist){
        return UpdatePlaylistDTO.builder()
                .playlistId(playlist.getId())
                .playlistName(playlist.getName())
                .build();
    }


    private static CastDTO convertToCastDTO(CastPlaylist castPlaylist) {
        return CastDTO.builder()
                .castId(castPlaylist.getCast().getId())
                .playlistId(castPlaylist.getPlaylist().getId())
                .castTitle(castPlaylist.getCast().getTitle())
                .isPublic(castPlaylist.getCast().getIsPublic())
                .castCreator(castPlaylist.getCast().getMember().getNickname())
                .castCategory(castPlaylist.getPlaylist().getName())
                .audioLength(castPlaylist.getCast().getAudioLength())
                .imagePath(castPlaylist.getCast().getImagePath())
                .build();
    }
}
