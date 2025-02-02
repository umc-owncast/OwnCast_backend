package com.umc.owncast.domain.playlist.template.crud;


import com.umc.owncast.domain.member.entity.Member;
import com.umc.owncast.domain.playlist.entity.Playlist;
import com.umc.owncast.domain.playlist.repository.PlaylistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public abstract class PlaylistCRUD {

    protected final PlaylistRepository playlistRepository;

    //TODO OBject를 사용해도 될까? 제네릭을 사용할까?
    public final Object execute(Member member, String playlistName, long playlistId){
        validate(member, playlistName, playlistId);
        return process(member, playlistName, playlistId);
    }

    protected abstract void validate(Member member, String playlistName, long playlistId);
    protected abstract Object process(Member member, String playlistName, long playlistId);

}
