package com.umc.owncast.domain.playlist.service;

import com.umc.owncast.common.exception.handler.UserHandler;
import com.umc.owncast.common.response.status.ErrorCode;
import com.umc.owncast.domain.playlist.dto.PlaylistDTO;
import com.umc.owncast.domain.playlist.entity.Playlist;
import com.umc.owncast.domain.playlist.repository.PlaylistRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class PlaylistAddServiceImpl {

    private final PlaylistRepository playlistRepository;

    public PlaylistDTO.AddPlaylistDTO addPlaylist(String categoryName) {
        // Long memberId = 토큰으로 정보 받아오기
        //임시로 1L로 설정

        if(playlistRepository.existsByName(categoryName)) {
            //에러
            throw new UserHandler(ErrorCode.PLAYLIST_ALREADY_EXIST);
        } else {
            Playlist newPlaylist = Playlist.builder()
                    .name(categoryName)
//                    .member()
                    .build();

            playlistRepository.save(newPlaylist);
            return PlaylistDTO.AddPlaylistDTO.builder()
                    .categoryId(newPlaylist.getId())
                    .build();
        }
    }
}
