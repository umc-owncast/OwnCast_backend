package com.umc.owncast.domain.playlist.template.crud;

import com.umc.owncast.common.exception.handler.UserHandler;
import com.umc.owncast.common.response.status.ErrorCode;
import com.umc.owncast.domain.member.entity.Member;
import com.umc.owncast.domain.playlist.dto.CreatePlaylistDTO;
import com.umc.owncast.domain.playlist.entity.Playlist;
import com.umc.owncast.domain.playlist.repository.PlaylistRepository;
import org.springframework.stereotype.Service;

import static com.umc.owncast.domain.playlist.factory.PlaylistDTOFactory.createCreatePlaylistDTO;

public class CreatePlaylist extends PlaylistCRUD{

    public CreatePlaylist(PlaylistRepository playlistRepository) {
        super(playlistRepository);
    }

    @Override
    protected void validate(Member member, String playlistName, long playlistId) {
        if (playlistRepository.existsByNameAndMemberId(playlistName, member.getId()))
            throw new UserHandler(ErrorCode.PLAYLIST_ALREADY_EXIST);
    }

    @Override
    protected CreatePlaylistDTO process(Member member, String playlistName, long playlistId) {

        Playlist newPlaylist = Playlist.builder()
                .name(playlistName)
                .member(member)
                .build();

        return createCreatePlaylistDTO(playlistRepository.save(newPlaylist).getId());
    }
}
