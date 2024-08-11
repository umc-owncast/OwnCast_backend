package com.umc.owncast.domain.cast.service;

import com.umc.owncast.common.exception.handler.UserHandler;
import com.umc.owncast.common.response.status.ErrorCode;
import com.umc.owncast.domain.cast.entity.Cast;
import com.umc.owncast.domain.cast.repository.CastRepository;
import com.umc.owncast.domain.castplaylist.entity.CastPlaylist;
import com.umc.owncast.domain.castplaylist.repository.CastPlaylistRepository;
import com.umc.owncast.domain.playlist.entity.Playlist;
import com.umc.owncast.domain.playlist.repository.PlaylistRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class CastSaveServiceImpl {
    private final PlaylistRepository playlistRepository;
    private final CastPlaylistRepository castPlaylistRepository;
    private final CastRepository castRepository;

    public void saveCast(Long castId) {
        // Long memberId = 토큰으로 정보 받아오기
        //임시로 1L로 설정

        Optional<Playlist> optionalPlaylist = playlistRepository.findSavedPlaylist(1L);
        //담아온 캐스트란 항목을 찾는다.
        Optional<Cast> optionalCast = castRepository.findById(castId);
        Cast cast;
        Playlist playlist;

        if (optionalCast.isEmpty() || optionalPlaylist.isEmpty()) {
            throw new UserHandler(ErrorCode.CAST_NOT_FOUND);
        } else {
            cast = optionalCast.get();
            playlist = optionalPlaylist.get();
        }

        if (castPlaylistRepository.existsByPlaylistIdAndCastId(playlist.getId(), cast.getId())) {
            throw new UserHandler(ErrorCode.CAST_ALREADY_EXIST);
        }
        ; // 이미 존재하는 경우

        CastPlaylist newCastPlaylist = CastPlaylist.builder()
                .cast(cast)
                .playlist(playlist)
                .id(1L)
                .build();

        castPlaylistRepository.save(newCastPlaylist);
    }
}
