package com.umc.owncast.domain.playlist.service;

import com.umc.owncast.domain.playlist.dto.*;

import java.util.List;


public interface PlaylistService {

    AddPlaylistDTO addPlaylist(String playlistName);

    DeletePlaylistDTO deletePlaylist(Long playlistId);

    GetPlaylistDTO getPlaylist(Long playlistId, int page, int size);

    ModifyPlaylistDTO modifyPlaylist(Long playlistId, String playlistName);

    List<PlaylistResultDTO> getAllPlaylists();
}
