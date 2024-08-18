package com.umc.owncast.domain.playlist.service;

import com.umc.owncast.common.exception.handler.UserHandler;
import com.umc.owncast.common.response.status.ErrorCode;
import com.umc.owncast.domain.cast.entity.Cast;
import com.umc.owncast.domain.cast.repository.CastRepository;
import com.umc.owncast.domain.castplaylist.entity.CastPlaylist;
import com.umc.owncast.domain.castplaylist.repository.CastPlaylistRepository;
import com.umc.owncast.domain.member.entity.Member;
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
    private static final String DEFAULT_IMAGE_PATH = "default/image/path";

    @Override
    public List<PlaylistResultDTO> getAllPlaylists(Member member) {

        List<Playlist> playlistList = playlistRepository.findAllByMemberIdOrderByCreatedAt(member.getId());
        List<PlaylistResultDTO> playlistDTOList = new ArrayList<>();
        Pageable pageable = PageRequest.of(0, 1);

        String myCastImagePath = castRepository.findFirstCastImage(member.getId())
                .orElse(DEFAULT_IMAGE_PATH);

        String mySavedCastImagePath = castPlaylistRepository.findFirstByPlaylist_Member_IdOrderByCreatedAt(member.getId(), pageable)
                .getContent()
                .stream()
                .findFirst()
                .orElse(DEFAULT_IMAGE_PATH);

        playlistDTOList.add(convertToPlaylistResultDTO("내가 만든 캐스트", myCastImagePath, null, null));

        playlistDTOList.add(convertToPlaylistResultDTO("담아온 캐스트", mySavedCastImagePath, null, null));

        playlistList.forEach(playlist -> {

            String playlistName = playlist.getName();
            String playlistImagePath = getOldestCastFromPlaylist(playlist.getId())
                    .map(Cast::getImagePath)
                    .orElse("default/image/path");
            Long playlistId = playlist.getId();
            Long totalCast = castPlaylistRepository.countAllByPlaylist(playlist);
            playlistDTOList.add(convertToPlaylistResultDTO(playlistName, playlistImagePath, playlistId, totalCast));

        });

        return playlistDTOList;
    }

    private PlaylistResultDTO convertToPlaylistResultDTO(String name, String imagePath, Long playlistId, Long totalCast) {
        return PlaylistResultDTO.builder()
                .name(name)
                .imagePath(imagePath)
                .playlistId(playlistId)
                .totalCast(totalCast)
                .build();
    }

    private Optional<Cast> getOldestCastFromPlaylist(Long playlistId) {
        Optional<CastPlaylist> optionalCast = castPlaylistRepository.findFirstByPlaylist_IdOrderByCreatedAt(playlistId);

        return optionalCast.map(CastPlaylist::getCast);
    }


    @Override
    @Transactional
    public AddPlaylistDTO addPlaylist(Member member, String playlistName) {

        if (playlistRepository.existsByNameAndMemberId(playlistName, member.getId())) {
            throw new UserHandler(ErrorCode.PLAYLIST_ALREADY_EXIST);  //에러
        } else {
            Playlist newPlaylist = Playlist.builder()
                    .name(playlistName)
                    .member(member)
                    .build();

            playlistRepository.save(newPlaylist);

            return AddPlaylistDTO.builder()
                    .playlistId(newPlaylist.getId())
                    .build();
        }
    }

    @Override
    @Transactional
    public DeletePlaylistDTO deletePlaylist(Member member, Long playlistId) {

        Optional<Playlist> optionalPlaylist = playlistRepository.findByIdAndMemberId(playlistId, member.getId());
        Playlist playlist;
        List<CastPlaylist> castPlaylists;

        if (optionalPlaylist.isEmpty()) {
            throw new UserHandler(ErrorCode.PLAYLIST_NOT_FOUND);
        } else {
            playlist = optionalPlaylist.get();

            // cast_playlist 엔티티에서 해당 플레이리스트 id를 가진 캐스트를 가져와 삭제
            castPlaylists = castPlaylistRepository.findAllByPlaylistId(playlistId);

            for (CastPlaylist castPlaylist : castPlaylists) {
                Long castMemberId = castPlaylist.getCast().getMember().getId();
                if(castMemberId.equals(member.getId())) {
                    Cast cast = castPlaylist.getCast();
                    // castPlaylist 와 관련된 모든 항목 삭제
                    castPlaylistRepository.deleteAllByCastId(cast.getId());
                    // 실제 cast 엔티티 삭제
                    castRepository.delete(cast);
                }
            }

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
    public GetPlaylistDTO getPlaylist(Member member, Long playlistId, int page, int size) {

        Optional<Playlist> optionalPlaylist = playlistRepository.findByIdAndMemberId(playlistId, member.getId());
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
    public ModifyPlaylistDTO modifyPlaylist(Member member, Long playlistId, String playlistName) {

        Optional<Playlist> optionalPlaylist = playlistRepository.findByIdAndMemberId(playlistId, member.getId());
        Playlist playlist;

        if (optionalPlaylist.isEmpty()) {
            throw new UserHandler(ErrorCode.PLAYLIST_NOT_FOUND);
        } else {
            playlist = optionalPlaylist.get();

            if (playlistRepository.existsByNameAndMemberId(playlistName, member.getId())) {
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
