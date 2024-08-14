package com.umc.owncast.domain.playlist.service;

import com.umc.owncast.common.exception.handler.UserHandler;
import com.umc.owncast.common.response.status.ErrorCode;
import com.umc.owncast.domain.cast.entity.Cast;
import com.umc.owncast.domain.cast.repository.CastRepository;
import com.umc.owncast.domain.castplaylist.entity.CastPlaylist;
import com.umc.owncast.domain.castplaylist.repository.CastPlaylistRepository;
import com.umc.owncast.domain.playlist.dto.*;
import com.umc.owncast.domain.playlist.entity.Playlist;
import com.umc.owncast.domain.playlist.repository.PlaylistRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class PlaylistServiceImpl implements PlaylistService {

    private final PlaylistRepository playlistRepository;
    private final CastRepository castRepository;
    private final CastPlaylistRepository castPlaylistRepository;

    @Override
    public List<PlaylistResultDTO> getAllPlaylists() {
        // Token으로 사용자 id 불러오기
        // 일단은 1L로 사용
        List<Playlist> playlistList = playlistRepository.findAllByMemberIdOrderByCreatedAt(1L);
        List<PlaylistResultDTO> playlistDTOList = new ArrayList<>();
        Pageable pageable = PageRequest.of(0, 1);

        playlistDTOList.add(
                PlaylistResultDTO.builder()
                        .name("내가 만든 캐스트")
                        .imagePath(castRepository.findFirstByMemberIdOrderByCreatedAt(1L).getImagePath())
                        .playlistId(null)
                        .totalCast(null)
                        .build()
        );

        playlistDTOList.add(
                PlaylistResultDTO.builder()
                        .name("담아온 캐스트")
                        .imagePath(
                                castPlaylistRepository.findFirstByPlaylist_Member_IdOrderByCreatedAt(1L, pageable)
                                        .getContent()
                                        .get(0)
                                        .getCast()
                                        .getImagePath()
                        )

                        .playlistId(null)
                        .totalCast(null)
                        .build()
        );

        playlistList.forEach(playlist -> {
            playlistDTOList.add(
                    PlaylistResultDTO.builder()
                            .name(playlist.getName())
                            .imagePath(getOldestCastFromPlaylist(playlist.getId())
                                    .map(Cast::getImagePath)
                                    .orElse("default/image/path")) // 기본 경로로 대체)
                            .playlistId(playlist.getId())
                            .totalCast(castPlaylistRepository.countAllByPlaylist(playlist))
                            .build()
            );
        });

        return playlistDTOList;
    }

    @Override
    public Optional<Cast> getOldestCastFromPlaylist(Long playlistId) {
        Optional<CastPlaylist> optionalCast = castPlaylistRepository.findFirstByPlaylist_IdOrderByCreatedAt(playlistId);

        return optionalCast.map(CastPlaylist::getCast);
    }


    @Override
    @Transactional
    public AddPlaylistDTO addPlaylist(String playlistName) {
        // Long memberId = 토큰으로 정보 받아오기
        //임시로 1L로 설정

        if (playlistRepository.existsByNameAndMemberId(playlistName, 1L)) {
            throw new UserHandler(ErrorCode.PLAYLIST_ALREADY_EXIST);  //에러
        } else {
            Playlist newPlaylist = Playlist.builder()
                    .name(playlistName)
//                    .member()
                    .build();

            playlistRepository.save(newPlaylist);

            return AddPlaylistDTO.builder()
                    .playlistId(newPlaylist.getId())
                    .build();
        }
    }

    @Override
    @Transactional
    public DeletePlaylistDTO deletePlaylist(Long playlistId) {
        // Long memberId = 토큰으로 정보 받아오기
        //임시로 1L로 설정

        Optional<Playlist> optionalPlaylist = playlistRepository.findByIdAndMemberId(playlistId, 1L);
        Playlist playlist;

        if (optionalPlaylist.isEmpty()) {
            throw new UserHandler(ErrorCode.PLAYLIST_NOT_FOUND);
        } else {
            playlist = optionalPlaylist.get();

            // cast_playlist 엔티티에서 해당 플레이리스트 id를 가진 모든 행을 삭제
            castPlaylistRepository.deleteAllByPlaylistId(playlistId);

            // playlist 엔티티에서 해당 플레이리스트 삭제
            playlistRepository.delete(playlist);

            return DeletePlaylistDTO.builder()
                    .playlistId(playlist.getId())
                    .build();
        }
    }

    @Override
    @Transactional
    public GetPlaylistDTO getPlaylist(Long playlistId, int page, int size) {
        // Long memberId = 토큰으로 정보 받아오기
        //임시로 1L로 설정

        Optional<Playlist> optionalPlaylist = playlistRepository.findByIdAndMemberId(playlistId, 1L);
        Page<CastPlaylist> castPlaylist;

        if (optionalPlaylist.isEmpty()) {
            throw new UserHandler(ErrorCode.PLAYLIST_NOT_FOUND);
        } else {
            // 페이지 요청 객체 생성
            Pageable pageable = PageRequest.of(page, size);
            castPlaylist = castPlaylistRepository.findByPlaylistId(playlistId, pageable);

            List<CastDTO> castDTOList = castPlaylist.getContent().stream()
                    .map(this::convertToCastDTO)
                    .collect(Collectors.toList());

            return GetPlaylistDTO.builder()
                    .castList(castDTOList)
                    .build();
        }
    }

    @Override
    @Transactional
    public ModifyPlaylistDTO modifyPlaylist(Long playlistId, String playlistName) {
        // Long memberId = 토큰으로 정보 받아오기
        //임시로 1L로 설정

        Optional<Playlist> optionalPlaylist = playlistRepository.findByIdAndMemberId(playlistId, 1L);
        Playlist playlist;

        if (optionalPlaylist.isEmpty()) {
            throw new UserHandler(ErrorCode.PLAYLIST_NOT_FOUND);
        } else {
            playlist = optionalPlaylist.get();

            if (playlistRepository.existsByNameAndMemberId(playlistName, 1L)) {
                throw new UserHandler(ErrorCode.PLAYLIST_ALREADY_EXIST);
            } else {
                playlist.setName(playlistName);
                playlistRepository.save(playlist);

                return ModifyPlaylistDTO.builder()
                        .playlistId(playlist.getId())
                        .playlistName(playlist.getName())
                        .build();
            }
        }
    }

    private CastDTO convertToCastDTO(CastPlaylist castPlaylist) {
        return CastDTO.builder()
                .castId(castPlaylist.getCast().getId())
                .castTitle(castPlaylist.getCast().getTitle())
                .isPublic(castPlaylist.getCast().getIsPublic())
                .castCreator(castPlaylist.getCast().getMember().getUsername())
                .castCategory(castPlaylist.getPlaylist().getName())
                .audioLength(castPlaylist.getCast().getAudioLength())
                .build();
    }
}
