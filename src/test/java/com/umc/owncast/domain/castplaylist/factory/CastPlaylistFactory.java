package com.umc.owncast.domain.castplaylist.factory;

import com.umc.owncast.domain.cast.entity.Cast;
import com.umc.owncast.domain.castplaylist.entity.CastPlaylist;
import com.umc.owncast.domain.member.entity.Member;
import com.umc.owncast.domain.playlist.entity.Playlist;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class CastPlaylistFactory {
    public static CastPlaylist createCastPlaylist(Cast cast, Playlist playlist) {
        return CastPlaylist.builder()
                .playlist(playlist)
                .cast(cast)
                .build();
    }

    public static List<CastPlaylist> createMultipleCastPlaylist(int count, Cast cast, Playlist playlist) {
        return IntStream.range(0, count)
                .mapToObj(i -> createCastPlaylist(cast, playlist)) // 메서드 이름 수정
                .collect(Collectors.toList());
    }
}
