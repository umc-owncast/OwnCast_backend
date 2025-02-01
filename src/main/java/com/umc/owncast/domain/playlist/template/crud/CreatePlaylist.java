package com.umc.owncast.domain.playlist.template.crud;

import com.umc.owncast.common.exception.handler.UserHandler;
import com.umc.owncast.common.response.status.ErrorCode;
import com.umc.owncast.domain.member.entity.Member;
import com.umc.owncast.domain.playlist.entity.Playlist;
import com.umc.owncast.domain.playlist.repository.PlaylistRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CreatePlaylist extends PlaylistCRUD{

    private final PlaylistRepository playlistRepository;

    @Override
    protected void validate(Member member, String playlistName, long playlistId) {
        if (playlistRepository.existsByNameAndMemberId(playlistName, member.getId()))
            throw new UserHandler(ErrorCode.PLAYLIST_ALREADY_EXIST);
    }

    @Override
    protected Long process(Member member, String playlistName, long playlistId) {

        Playlist newPlaylist = Playlist.builder()
                .name(playlistName)
                .member(member)
                .build();

        return playlistRepository.save(newPlaylist).getId();
    }
}
