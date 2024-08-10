package com.umc.owncast.domain.playlist.service;

import com.umc.owncast.common.exception.handler.UserHandler;
import com.umc.owncast.common.response.status.ErrorCode;
import com.umc.owncast.domain.castplaylist.entity.CastPlaylist;
import com.umc.owncast.domain.castplaylist.repository.CastPlaylistRepository;
import com.umc.owncast.domain.playlist.dto.PlaylistDTO;
import com.umc.owncast.domain.playlist.entity.Playlist;
import com.umc.owncast.domain.playlist.repository.PlaylistRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class PlaylistGetServiceImpl {
    private final PlaylistRepository playlistRepository;
    private final CastPlaylistRepository castPlaylistRepository;

    public PlaylistDTO.GetPlaylistDTO getPlaylist(Long playlistId, int page, int size) {
        // Long memberId = 토큰으로 정보 받아오기
        //임시로 1L로 설정

        Optional<Playlist> optionalPlaylist = playlistRepository.findByIdAndMemberId(playlistId, 1L);
        Playlist playlist;
        Page<CastPlaylist> castPlaylist;

        if(optionalPlaylist.isEmpty()) {
            throw new UserHandler(ErrorCode.PLAYLIST_NOT_FOUND);
        } else {
            playlist = optionalPlaylist.get();

            // 페이지 요청 객체 생성
            Pageable pageable = PageRequest.of(page, size);
            castPlaylist = castPlaylistRepository.findByPlaylistId(playlistId, pageable);

            List<PlaylistDTO.CastDTO> castDTOList = castPlaylist.getContent().stream()
                    .map(this::convertToCastDTO)
                    .collect(Collectors.toList());

            return PlaylistDTO.GetPlaylistDTO.builder()
                    .castList(castDTOList)
                    .build();
        }
    }

    private PlaylistDTO.CastDTO convertToCastDTO(CastPlaylist castPlaylist) {
        return PlaylistDTO.CastDTO.builder()
                .castId(castPlaylist.getCast().getId())
                .castTitle(castPlaylist.getCast().getTitle())
                .isPublic(castPlaylist.getCast().isPublic())
                .castCreator(castPlaylist.getCast().getMember().getUsername())
                .castCategory(castPlaylist.getPlaylist().getName())
                .audioLength(castPlaylist.getCast().getAudioLength())
                .build();
    }
}
