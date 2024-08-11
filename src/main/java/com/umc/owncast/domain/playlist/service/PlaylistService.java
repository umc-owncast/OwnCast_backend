package com.umc.owncast.domain.playlist.service;

import com.umc.owncast.domain.playlist.dto.PlaylistDTO;

import java.util.List;

public interface PlaylistService {
    public List<PlaylistDTO.PlaylistResultDTO> getAllPlaylists();
}
