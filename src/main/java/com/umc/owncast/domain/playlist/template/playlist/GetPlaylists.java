package com.umc.owncast.domain.playlist.template.playlist;

import com.umc.owncast.domain.castplaylist.repository.CastPlaylistRepository;
import com.umc.owncast.domain.member.entity.Member;
import com.umc.owncast.domain.playlist.dto.CastDTO;
import com.umc.owncast.domain.castplaylist.entity.CastPlaylist;
import com.umc.owncast.domain.playlist.factory.PlaylistDTOFactory;
import com.umc.owncast.domain.playlist.repository.PlaylistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@RequiredArgsConstructor
@Service
public abstract class GetPlaylists {

    // TODO final 붙이면 super 해야하는 이유?
    protected final CastPlaylistRepository castPlaylistRepository;
    protected final PlaylistRepository playlistRepository;

    protected final int pageSize = 5;

    //TODO 메소드 오버로딩을 활용하는게 나을까 아니면 인자를 null로 넘기는게 나을까
    //TODO 나중에 CRUD랑 합치기
    public final List<CastDTO> get(Member member, long playlistId, int page) {
        return PlaylistDTOFactory.createCastDTOList(fetchData(member, playlistId, page));
    }

    protected abstract List<CastPlaylist> fetchData(Member member, long playlistId, int page);
}
