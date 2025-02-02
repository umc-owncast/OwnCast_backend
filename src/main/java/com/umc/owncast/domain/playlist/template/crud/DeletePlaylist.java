package com.umc.owncast.domain.playlist.template.crud;

import com.umc.owncast.common.exception.handler.UserHandler;
import com.umc.owncast.common.response.status.ErrorCode;
import com.umc.owncast.domain.castplaylist.repository.CastPlaylistRepository;
import com.umc.owncast.domain.member.entity.Member;
import com.umc.owncast.domain.member.repository.MemberRepository;
import com.umc.owncast.domain.playlist.entity.Playlist;
import com.umc.owncast.domain.playlist.factory.PlaylistDTOFactory;
import com.umc.owncast.domain.playlist.repository.PlaylistRepository;
import lombok.RequiredArgsConstructor;

public class DeletePlaylist extends PlaylistCRUD {

    public DeletePlaylist(PlaylistRepository playlistRepository) {
        super(playlistRepository);
    }

    @Override
    protected void validate(Member member, String playlistName, long playlistId) {
        playlistRepository.findByIdAndMemberId(playlistId, member.getId()).orElseThrow(() ->
                new UserHandler(ErrorCode.PLAYLIST_NOT_FOUND));
    }

    @Override
    protected Object process(Member member, String playlistName, long playlistId) {

        // 양뱡향 매핑. CastPlaylist에서도 지워짐
        playlistRepository.deleteById(playlistId);

        return PlaylistDTOFactory.createDeletePlaylistDTO(playlistId);
    }
}
