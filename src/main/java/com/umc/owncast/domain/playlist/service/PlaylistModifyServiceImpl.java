package com.umc.owncast.domain.playlist.service;

import com.umc.owncast.common.exception.handler.UserHandler;
import com.umc.owncast.common.response.status.ErrorCode;
import com.umc.owncast.domain.playlist.dto.PlaylistDTO;
import com.umc.owncast.domain.playlist.entity.Playlist;
import com.umc.owncast.domain.playlist.repository.PlaylistRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class PlaylistModifyServiceImpl {
    private final PlaylistRepository playlistRepository;

    public PlaylistDTO.ModifyPlaylistDTO modifyPlaylist(Long playlistId, String playlistName) {
        // Long memberId = 토큰으로 정보 받아오기
        //임시로 1L로 설정

        Optional<Playlist> optionalPlaylist = playlistRepository.findByIdAndMemberId(playlistId, 1L);
        Playlist playlist;

        if(optionalPlaylist.isEmpty()) {
            throw new UserHandler(ErrorCode.PLAYLIST_NOT_FOUND);
        } else {
            playlist = optionalPlaylist.get();

            if(playlistRepository.existsByNameAndMemberId(playlistName, 1L)) {
                throw new UserHandler(ErrorCode.PLAYLIST_ALREADY_EXIST);
            }
            else {
                playlist.setName(playlistName);
                playlistRepository.save(playlist);

                return PlaylistDTO.ModifyPlaylistDTO.builder()
                        .playlistId(playlist.getId())
                        .playlistName(playlist.getName())
                        .build();
            }
        }

    }
}
