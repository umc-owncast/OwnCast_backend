package com.umc.owncast.domain.playlist.service;

import com.umc.owncast.domain.member.entity.Member;
import com.umc.owncast.domain.playlist.dto.*;

import java.util.List;

public interface PlaylistService {

    CreatePlaylistDTO addPlaylist(Member member, String playlistName);

    UpdatePlaylistDTO updatePlaylist(Member member, Long playlistId, String playlistName);

    DeletePlaylistDTO deletePlaylist(Member member, Long playlistId);

    List<CastDTO> getPlaylist(Member member, Long playlistId, int page);

    List<PlaylistResultDTO> getAllPlaylists(Member member);

    List<CastDTO> getAllSavedPlaylists(Member member, int page);

    List<CastDTO> getAllMyPlaylists(Member member, int page);

    DeleteCastFromPlaylistDTO deleteCast(Long playlistId, Long castId, Member member);

    /*void updateImage();*/
}
