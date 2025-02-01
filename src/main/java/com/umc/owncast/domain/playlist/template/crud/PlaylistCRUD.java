package com.umc.owncast.domain.playlist.template.crud;


import com.umc.owncast.domain.member.entity.Member;

public abstract class PlaylistCRUD {

    //TODO OBject를 사용해도 될까? 제네릭을 사용할까?
    public final Object execute(Member member, String playlistName, long playlistId){
        validate(member, playlistName, playlistId);
        return process(member, playlistName, playlistId);
    }

    protected abstract void validate(Member member, String playlistName, long playlistId);
    protected abstract Object process(Member member, String playlistName, long playlistId);

}
