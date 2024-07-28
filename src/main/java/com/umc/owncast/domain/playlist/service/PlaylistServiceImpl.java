package com.umc.owncast.domain.playlist.service;

import com.umc.owncast.domain.bookmark.dto.BookMarkDTO;
import com.umc.owncast.domain.playlist.dto.PlaylistDTO;
import com.umc.owncast.domain.playlist.repository.PlaylistRepository;
import com.umc.owncast.domain.playlist.entity.Playlist;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class PlaylistServiceImpl {
    private final PlaylistRepository playlistRepository;

    public List<PlaylistDTO.PlaylistResultDTO> getAllPlaylists(){
        // Token으로 사용자 id 불러오기
        // 일단은 1L로 사용
        List<Playlist> playlistList = playlistRepository.findAllByMemberIdOrderByCreatedAt(1L);

        return playlistList.stream().map(playlist ->
                PlaylistDTO.PlaylistResultDTO.builder()
                        .name(playlist.getName())
                        .playlistId(playlist.getId())
                        .build()
        ).toList();
    }

}
