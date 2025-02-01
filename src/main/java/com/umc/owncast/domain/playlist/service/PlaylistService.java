package com.umc.owncast.domain.playlist.service;

import com.umc.owncast.domain.member.entity.Member;
import com.umc.owncast.domain.playlist.dto.*;

import java.util.List;

public interface PlaylistService {

    Long addPlaylist(Member member, String playlistName);

    DeletePlaylistDTO deletePlaylist(Member member, Long playlistId);

    List<CastDTO> getPlaylist(Member member, Long playlistId, int page);

    ModifyPlaylistDTO modifyPlaylist(Member member, Long playlistId, String playlistName);

    List<PlaylistResultDTO> getAllPlaylists(Member member);

    List<CastDTO> getAllSavedPlaylists(Member member, int page);

    List<CastDTO> getAllMyPlaylists(Member member, int page);

    DeleteCastFromPlaylistDTO deleteCast(Long playlistId, Long castId, Member member);

    /*void updateImage();*/
}
