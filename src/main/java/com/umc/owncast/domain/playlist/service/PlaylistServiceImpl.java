package com.umc.owncast.domain.playlist.service;

import com.umc.owncast.common.annotation.TrackExecutionTime;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class PlaylistServiceImpl implements PlaylistService {

    private final PlaylistRepository playlistRepository;
    private final CastRepository castRepository;
    private final CastPlaylistRepository castPlaylistRepository;

    @Value("${app.image.default-path}")
    private String DEFAULT_IMAGE_PATH;

    @Override
    public List<PlaylistResultDTO> getAllPlaylists(Member member) {

        List<Playlist> playlistList = playlistRepository.findAllByMemberIdOrderByCreatedAt(member.getId());
        List<PlaylistResultDTO> playlistDTOList = new ArrayList<>();
        Pageable pageable = PageRequest.of(0, 1);

        Cast myCastImagePath = castPlaylistRepository.findFirstSavedCast(member.getId(), pageable)
                .getContent()
                .stream()
                .findFirst()
                .orElse(Cast.builder().imagePath(DEFAULT_IMAGE_PATH).build());

        Cast mySavedCastImagePath = castPlaylistRepository.findFirstOtherCast(member.getId(), pageable)
                .getContent()
                .stream()
                .findFirst()
                .orElse(Cast.builder().imagePath(DEFAULT_IMAGE_PATH).build());

        /* CAST 이미지 경로 값 자체가 null인 경우를 해결해야 함 */

        playlistDTOList.add(convertToPlaylistResultDTO("내가 만든 캐스트", myCastImagePath.getImagePath(), null, null));

        playlistDTOList.add(convertToPlaylistResultDTO("담아온 캐스트", mySavedCastImagePath.getImagePath(), null, null));

        playlistList.forEach(playlist -> {
            String playlistName = playlist.getName();
            String playlistImagePath = getOldestCastFromPlaylist(playlist.getId())
                    .map(Cast::getImagePath)
                    .orElse(DEFAULT_IMAGE_PATH);
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

        if (playlistRepository.existsByNameAndMemberId(playlistName, member.getId()))
            throw new UserHandler(ErrorCode.PLAYLIST_ALREADY_EXIST);

        Playlist newPlaylist = Playlist.builder()
                .name(playlistName)
                .member(member)
                .build();

        log.info("새 플레이리스트 생성 {}", newPlaylist.getName());

        playlistRepository.save(newPlaylist);

        return AddPlaylistDTO.builder()
                .playlistId(newPlaylist.getId())
                .build();

    }

    @Override
    @Transactional
    public DeletePlaylistDTO deletePlaylist(Member member, Long playlistId) {

        Playlist playlist = playlistRepository.findByIdAndMemberId(playlistId, member.getId()).orElseThrow(() ->
                new UserHandler(ErrorCode.PLAYLIST_NOT_FOUND));
        List<CastPlaylist> castPlaylists;

        // cast_playlist 엔티티에서 해당 플레이리스트 id를 가진 캐스트를 가져와 삭제
        castPlaylists = castPlaylistRepository.findAllByPlaylistId(playlistId);

        for (CastPlaylist castPlaylist : castPlaylists) {
            Long castMemberId = castPlaylist.getCast().getMember().getId();
            if(!castMemberId.equals(member.getId()))
                throw new UserHandler(ErrorCode.PLAYLIST_UNAUTHORIZED_ACCESS);

            Cast cast = castPlaylist.getCast();
            castPlaylistRepository.deleteAllByCast(cast);
            castRepository.delete(cast);
        }

        // cast_playlist 엔티티에서 해당 플레이리스트 id를 가진 모든 행을 삭제
        castPlaylistRepository.deleteAllByPlaylistId(playlistId);

        // playlist 엔티티에서 해당 플레이리스트 삭제
        playlistRepository.delete(playlist);

        return DeletePlaylistDTO.builder()
                .playlistId(playlist.getId())
                .build();

    }


    @Override
    @Transactional
    public GetPlaylistDTO getPlaylist(Member member, Long playlistId, int page, int size) {

        Playlist playlist = playlistRepository.findByIdAndMemberId(playlistId, member.getId()).orElseThrow(() ->
                new UserHandler(ErrorCode.PLAYLIST_NOT_FOUND));
        Page<CastPlaylist> castPlaylist;

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

    @Override
    public GetPlaylistDTO getAllSavedPlaylists(Member member) {
        List<CastPlaylist> savedCastList = castPlaylistRepository.findAllSavedCast(member.getId());

        List<CastDTO> castDTOList = savedCastList.stream()
                .map(this::convertToCastDTO)
                .toList();

        return GetPlaylistDTO.builder()
                .castList(castDTOList)
                .build();
    }

    @Override
    public GetPlaylistDTO getAllMyPlaylists(Member member) {
        List<CastPlaylist> savedCastList = castPlaylistRepository.findAllMyCast(member.getId());

        List<CastDTO> castDTOList = savedCastList.stream()
                .map(this::convertToCastDTO)
                .toList();

        return GetPlaylistDTO.builder()
                .castList(castDTOList)
                .build();
    }

    @Override
    @Transactional
    public DeleteCastFromPlaylistDTO deleteCast(Long playlistId, Long castId, Member member) {
        Cast cast = castRepository.findById(castId).orElseThrow(() -> new UserHandler(ErrorCode.CAST_NOT_FOUND));
        Playlist playlist = playlistRepository.findById(playlistId).orElseThrow(() -> new UserHandler(ErrorCode.PLAYLIST_NOT_FOUND));

        if(!Objects.equals(playlist.getMember().getId(), member.getId())){ // 본인의 플레이리스트가 아닌 경우
            throw new UserHandler(ErrorCode.PLAYLIST_UNAUTHORIZED_ACCESS);
        }

        if(Objects.equals(cast.getMember().getId(), playlist.getMember().getId())) { // 본인이 만든 캐스트인 경우
            throw new UserHandler(ErrorCode._BAD_REQUEST);
        }

        castPlaylistRepository.deleteByCastAndPlaylist(cast, playlist);

        return DeleteCastFromPlaylistDTO.builder()
                .castId(castId)
                .build();
    }

    @Override
    @Transactional
    public ModifyPlaylistDTO modifyPlaylist(Member member, Long playlistId, String playlistName) {

        Playlist playlist = playlistRepository.findByIdAndMemberId(playlistId, member.getId()).orElseThrow(() ->
                new UserHandler(ErrorCode.PLAYLIST_NOT_FOUND));

        if(!playlist.getMember().getId().equals(member.getId()))
            throw new UserHandler(ErrorCode.PLAYLIST_UNAUTHORIZED_ACCESS);

        if (playlistRepository.existsByNameAndMemberId(playlistName, member.getId()))
            throw new UserHandler(ErrorCode.PLAYLIST_ALREADY_EXIST);

        playlist.setName(playlistName);
        playlistRepository.save(playlist);

        return ModifyPlaylistDTO.builder()
                .playlistId(playlist.getId())
                .playlistName(playlist.getName())
                .build();
    }

    private CastDTO convertToCastDTO(CastPlaylist castPlaylist) {
        return CastDTO.builder()
                .castId(castPlaylist.getCast().getId())
                .playlistId(castPlaylist.getPlaylist().getId())
                .castTitle(castPlaylist.getCast().getTitle())
                .isPublic(castPlaylist.getCast().getIsPublic())
                .castCreator(castPlaylist.getCast().getMember().getNickname())
                .castCategory(castPlaylist.getPlaylist().getName())
                .audioLength(castPlaylist.getCast().getAudioLength())
                .imagePath(castPlaylist.getCast().getImagePath())
                .build();
    }
}
