package com.umc.owncast.domain.playlist.template.crud;

import com.umc.owncast.common.exception.handler.UserHandler;
import com.umc.owncast.common.response.status.ErrorCode;
import com.umc.owncast.domain.member.entity.Member;
import com.umc.owncast.domain.playlist.dto.CreatePlaylistDTO;
import com.umc.owncast.domain.playlist.dto.UpdatePlaylistDTO;
import com.umc.owncast.domain.playlist.entity.Playlist;
import com.umc.owncast.domain.playlist.repository.PlaylistRepository;
import lombok.RequiredArgsConstructor;

import static com.umc.owncast.domain.playlist.factory.PlaylistDTOFactory.createCreatePlaylistDTO;
import static com.umc.owncast.domain.playlist.factory.PlaylistDTOFactory.createUpdatePlaylistDTO;

public class UpdatePlaylist extends PlaylistCRUD {

    public UpdatePlaylist(PlaylistRepository playlistRepository) {
        super(playlistRepository);
    }

    @Override
    protected void validate(Member member, String playlistName, long playlistId) {

        // TODO 에러 핸들러 리팩토링

        Playlist playlist = playlistRepository.findByIdAndMemberId(playlistId, member.getId()).orElseThrow(() ->
                new UserHandler(ErrorCode.PLAYLIST_NOT_FOUND));

        if(!playlist.getMember().getId().equals(member.getId()))
            throw new UserHandler(ErrorCode.PLAYLIST_UNAUTHORIZED_ACCESS);

        if (playlistRepository.existsByNameAndMemberId(playlistName, member.getId()))
            throw new UserHandler(ErrorCode.PLAYLIST_ALREADY_EXIST);
    }

    @Override
    protected UpdatePlaylistDTO process(Member member, String playlistName, long playlistId) {

        // TODO playlist 가 execute, process 두번 검색되는데, 나중에 한번만 검색해도 되도록 리팩토링
        Playlist playlist = playlistRepository.findByIdAndMemberId(playlistId, member.getId()).orElseThrow(() ->
                new UserHandler(ErrorCode.PLAYLIST_NOT_FOUND));

        playlist.setName(playlistName);

        playlistRepository.save(playlist);

        return createUpdatePlaylistDTO(playlistRepository.save(playlist));
    }
}
