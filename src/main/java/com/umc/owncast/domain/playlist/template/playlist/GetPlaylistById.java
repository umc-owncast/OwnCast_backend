package com.umc.owncast.domain.playlist.template.playlist;

import com.umc.owncast.common.exception.handler.UserHandler;
import com.umc.owncast.common.response.status.ErrorCode;
import com.umc.owncast.domain.castplaylist.entity.CastPlaylist;
import com.umc.owncast.domain.castplaylist.repository.CastPlaylistRepository;
import com.umc.owncast.domain.member.entity.Member;
import com.umc.owncast.domain.playlist.repository.PlaylistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

public class GetPlaylistById extends GetPlaylists {

    // TODO super 이부분 고치기
    public GetPlaylistById(CastPlaylistRepository castPlaylistRepository, PlaylistRepository playlistRepository) {
        super(castPlaylistRepository, playlistRepository);
    }

    @Override
    protected List<CastPlaylist> fetchData(Member member, long playlistId, int page) {
        // 예외처리
        playlistRepository.findByIdAndMemberId(playlistId, member.getId()).orElseThrow(() -> new UserHandler(ErrorCode.PLAYLIST_NOT_FOUND));

        Pageable pageable = PageRequest.of(page - 1, pageSize);

        return castPlaylistRepository.findByPlaylistId(playlistId, pageable).getContent();
    }

}
