package com.umc.owncast.domain.playlist.service;

import com.umc.owncast.domain.bookmark.dto.BookMarkDTO;
import com.umc.owncast.domain.cast.entity.Cast;
import com.umc.owncast.domain.cast.repository.CastRepository;
import com.umc.owncast.domain.castplaylist.repository.CastPlaylistRepository;
import com.umc.owncast.domain.playlist.dto.PlaylistDTO;
import com.umc.owncast.domain.playlist.repository.PlaylistRepository;
import com.umc.owncast.domain.playlist.entity.Playlist;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class PlaylistServiceImpl {

    private final PlaylistRepository playlistRepository;
    private final CastRepository castRepository;
    private final CastPlaylistRepository castPlaylistRepository;

    public List<PlaylistDTO.PlaylistResultDTO> getAllPlaylists(){

        // Token으로 사용자 id 불러오기
        // 일단은 1L로 사용
        List<Playlist> playlistList = playlistRepository.findAllByMemberIdOrderByCreatedAt(1L);
        List<PlaylistDTO.PlaylistResultDTO> playlistDTOList = new ArrayList<>();

        playlistDTOList.add(
                PlaylistDTO.PlaylistResultDTO.builder()
                        .name("내가 만든 캐스트")
                        .imagePath(castRepository.findFirstByMemberIdOrderByCreatedAt(1L).getImagePath())
                        .playlistId(null)
                        .totalCast(null)
                        .build()
        );

        playlistDTOList.add(
                PlaylistDTO.PlaylistResultDTO.builder()
                        .name("담아온 캐스트")
                        .imagePath(castPlaylistRepository.findFirstByPlaylist_Member_IdOrderByCreatedAt(1L).getPlaylist().getImagePath())
                        .playlistId(null)
                        .totalCast(null)
                        .build()
        );

        playlistList.forEach(playlist ->
                playlistDTOList.add(
                        PlaylistDTO.PlaylistResultDTO.builder()
                                .name(playlist.getName())
                                .imagePath(playlist.getImagePath())
                                .playlistId(playlist.getId())
                                .totalCast(castPlaylistRepository.countAllByPlaylist(playlist))
                                .build()
                )
        );

        return playlistDTOList;
    }

}
