package com.umc.owncast.domain.playlist.template;

import com.umc.owncast.common.exception.handler.UserHandler;
import com.umc.owncast.common.response.status.ErrorCode;
import com.umc.owncast.domain.castplaylist.entity.CastPlaylist;
import com.umc.owncast.domain.castplaylist.repository.CastPlaylistRepository;
import com.umc.owncast.domain.member.entity.Member;
import com.umc.owncast.domain.playlist.entity.Playlist;
import com.umc.owncast.domain.playlist.repository.PlaylistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
public class GetAllMyPlaylist extends GetPlaylists {

    private final CastPlaylistRepository castPlaylistRepository;

    @Override
    protected List<CastPlaylist> fetchData(Member member, long playlistId, int page) {

        Pageable pageable = PageRequest.of(page - 1, pageSize);

        return castPlaylistRepository.findAllMyCast(member.getId(), pageable).getContent();
    }
}
