package com.umc.owncast.domain.playlist.service;

import com.umc.owncast.domain.playlist.dto.PlaylistDTO;

public interface PlaylistService {

    public PlaylistDTO.AddPlaylistDTO addPlaylist(String playlistName);

    public PlaylistDTO.DeletePlaylistDTO deletePlaylist(Long playlistId);

    public PlaylistDTO.GetPlaylistDTO getPlaylist(Long playlistId, int page, int size);

    public PlaylistDTO.ModifyPlaylistDTO modifyPlaylist(Long playlistId, String playlistName);
}
