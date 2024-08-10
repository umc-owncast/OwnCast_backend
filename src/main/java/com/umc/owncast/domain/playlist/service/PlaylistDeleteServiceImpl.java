package com.umc.owncast.domain.playlist.service;

import com.umc.owncast.common.exception.handler.UserHandler;
import com.umc.owncast.common.response.status.ErrorCode;
import com.umc.owncast.domain.castplaylist.repository.CastPlaylistRepository;
import com.umc.owncast.domain.playlist.dto.PlaylistDTO;
import com.umc.owncast.domain.playlist.entity.Playlist;
import com.umc.owncast.domain.playlist.repository.PlaylistRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class PlaylistDeleteServiceImpl {
    private final PlaylistRepository playlistRepository;
    private final CastPlaylistRepository castPlaylistRepository;

    @Transactional
    public PlaylistDTO.DeletePlaylistDTO deletePlaylist(Long playlistId) {
        // Long memberId = 토큰으로 정보 받아오기
        //임시로 1L로 설정

        Optional<Playlist> optionalPlaylist = playlistRepository.findByIdAndMemberId(playlistId, 1L);
        Playlist playlist;

        if(optionalPlaylist.isEmpty()) {
            throw new UserHandler(ErrorCode.PLAYLIST_NOT_FOUND);
        } else {
            playlist = optionalPlaylist.get();

            // TODO: cast_playlist 엔티티에서 해당 플레이리스트 id를 가진 모든 행을 삭제
//            castPlaylistRepository.deleteById(playlistId);
//            castPlaylistRepository.deleteByPlaylistIdAndMemberId(playlistId, 1L, playlistRepository);
            // playlist 엔티티에서 해당 플레이리스트 삭제
            playlistRepository.delete(playlist);

            return PlaylistDTO.DeletePlaylistDTO.builder()
                    .playlistId(playlist.getId())
                    .build();
        }

    }
}
