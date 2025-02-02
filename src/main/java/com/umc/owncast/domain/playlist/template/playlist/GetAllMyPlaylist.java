package com.umc.owncast.domain.playlist.template.playlist;

import com.umc.owncast.domain.castplaylist.entity.CastPlaylist;
import com.umc.owncast.domain.castplaylist.repository.CastPlaylistRepository;
import com.umc.owncast.domain.member.entity.Member;
import com.umc.owncast.domain.playlist.repository.PlaylistRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

public class GetAllMyPlaylist extends GetPlaylists {

    public GetAllMyPlaylist(CastPlaylistRepository castPlaylistRepository, PlaylistRepository playlistRepository) {
        super(castPlaylistRepository, playlistRepository);
    }

    @Override
    protected List<CastPlaylist> fetchData(Member member, long playlistId, int page) {

        Pageable pageable = PageRequest.of(page - 1, pageSize);

        return castPlaylistRepository.findAllMyCast(member.getId(), pageable).getContent();
    }
}
