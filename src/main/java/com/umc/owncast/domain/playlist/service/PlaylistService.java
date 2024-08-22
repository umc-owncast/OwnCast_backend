package com.umc.owncast.domain.playlist.service;

import com.umc.owncast.domain.cast.entity.Cast;
import com.umc.owncast.domain.member.entity.Member;
import com.umc.owncast.domain.playlist.dto.*;

import java.util.List;
import java.util.Optional;

public interface PlaylistService {

    AddPlaylistDTO addPlaylist(Member member, String playlistName);

    DeletePlaylistDTO deletePlaylist(Member member, Long playlistId);

    GetPlaylistDTO getPlaylist(Member member, Long playlistId, int page, int size);

    ModifyPlaylistDTO modifyPlaylist(Member member, Long playlistId, String playlistName);

    List<PlaylistResultDTO> getAllPlaylists(Member member);

    GetPlaylistDTO getAllSavedPlaylists(Member member);

    GetPlaylistDTO getAllMyPlaylists(Member member);
}
