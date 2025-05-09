package com.umc.owncast.domain.playlist.factory;

import com.umc.owncast.domain.cast.entity.Cast;
import com.umc.owncast.domain.enums.Formality;
import com.umc.owncast.domain.enums.Language;
import com.umc.owncast.domain.member.entity.Member;
import com.umc.owncast.domain.playlist.entity.Playlist;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class PlaylistFactory {
    public static Playlist createPlaylist(Member member, String name){
        return Playlist.builder()
                .member(member)
                .name(name)
                .imagePath(null)
                .isPublic(true)
                .build();
    }

    public static List<Playlist> createMultiplePlaylist(int count, Member member, String name) {
        return IntStream.range(0, count)
                .mapToObj(i -> createPlaylist(member, name + i)) // 메서드 이름 수정
                .collect(Collectors.toList());
    }
}
