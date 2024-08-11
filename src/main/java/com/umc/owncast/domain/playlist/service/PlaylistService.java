package com.umc.owncast.domain.playlist.service;

import com.umc.owncast.domain.playlist.dto.PlaylistDTO;

import java.util.List;


public interface PlaylistService {

    PlaylistDTO.AddPlaylistDTO addPlaylist(String playlistName);

    PlaylistDTO.DeletePlaylistDTO deletePlaylist(Long playlistId);

    PlaylistDTO.GetPlaylistDTO getPlaylist(Long playlistId, int page, int size);

    PlaylistDTO.ModifyPlaylistDTO modifyPlaylist(Long playlistId, String playlistName);

    List<PlaylistDTO.PlaylistResultDTO> getAllPlaylists();
}
