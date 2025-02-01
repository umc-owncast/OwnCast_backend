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
import com.umc.owncast.domain.playlist.factory.PlaylistDTOFactory;
import com.umc.owncast.domain.playlist.repository.PlaylistRepository;
import com.umc.owncast.domain.playlist.template.GetAllMyPlaylist;
import com.umc.owncast.domain.playlist.template.GetPlaylistById;
import com.umc.owncast.domain.playlist.template.GetPlaylists;
import com.umc.owncast.domain.playlist.template.GetSavedPlaylist;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
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
    private GetPlaylists getPlaylists;

    @Value("${app.image.default-path}")
    private String DEFAULT_IMAGE_PATH;

    @Override
    @Transactional
    public Long addPlaylist(Member member, String playlistName) {

        if (playlistRepository.existsByNameAndMemberId(playlistName, member.getId()))
            throw new UserHandler(ErrorCode.PLAYLIST_ALREADY_EXIST);

        Playlist newPlaylist = Playlist.builder()
                .name(playlistName)
                .member(member)
                .build();

        log.info("새 플레이리스트 생성 {}", newPlaylist.getName());

        playlistRepository.save(newPlaylist);

        return newPlaylist.getId();
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

    @Override
    @TrackExecutionTime
    public List<PlaylistResultDTO> getAllPlaylists(Member member) {

        // playlistDTOList 초기화
        List<PlaylistResultDTO> playlistDTOList = new ArrayList<>();

        // 기본 캐스트 리스트 추가
        addDefaultPlaylists(playlistDTOList);

        // 모든 플레이리스트 추가
        playlistDTOList.addAll(PlaylistDTOFactory.createMultiplePlaylistResultDTO(playlistRepository.findAllByMember(member)));

        return playlistDTOList;
    }

    @Override
    @Transactional
    public List<CastDTO> getPlaylist(Member member, Long playlistId, int page) {

        //TODO 의존성 주입이 나을지?
        getPlaylists = new GetPlaylistById(castPlaylistRepository, playlistRepository);

        return getPlaylists.get(member, playlistId, page);

    }

    @Override
    public List<CastDTO> getAllSavedPlaylists(Member member, int page) {

        getPlaylists = new GetSavedPlaylist(castPlaylistRepository);

        return getPlaylists.get(member, 0, page);
    }

    @Override
    public List<CastDTO> getAllMyPlaylists(Member member, int page) {
        getPlaylists = new GetAllMyPlaylist(castPlaylistRepository);

        return getPlaylists.get(member, 0, page);
    }


    /*@Override
    @Scheduled(fixedRate = 3600000)
    @TrackExecutionTime
    public void updateImage(){

        log.info("플레이리스트에 이미지를 넣는 중");

        List<Playlist> playlists = playlistRepository.findAllByImagePathIsNull();

        log.info("이미지를 넣어야 할 플레이리스트의 수 : {}", playlists.size());

        // image_path가 null인 플레이리스트만 가져오기
        playlists.forEach(playlist -> {
                    // 가장 오래된 CastPlay를 조회하여 imagePath를 설정
                    String imagePath = castPlaylistRepository.findFirstByPlaylist_IdOrderByCreatedAt(playlist.getId())
                            .map(castPlay -> castPlay.getCast().getImagePath())
                            .orElse(null);

                    log.info("이미지를 찾았습니다 : {}", imagePath);

                    playlist.setImagePath(imagePath);

                    // 업데이트된 playlist 저장
                    playlistRepository.save(playlist);
                });
    }*/

    // 기본 캐스트 리스트를 추가하는 메서드
    private void addDefaultPlaylists(List<PlaylistResultDTO> playlistDTOList) {
        playlistDTOList.add(PlaylistDTOFactory.createPlaylistResultDTO("내가 만든 캐스트", DEFAULT_IMAGE_PATH, null, null)); // TODO 이것도 개선 해야 됨.
        playlistDTOList.add(PlaylistDTOFactory.createPlaylistResultDTO("담아온 캐스트", DEFAULT_IMAGE_PATH, null, null));
    }

    // 가장 오래된 캐스트의 이미지 가져오기
    private String getPlaylistImage(Playlist playlist){
        if(playlist.getCastSize() == 0){
            return DEFAULT_IMAGE_PATH;
        } else {
            return castPlaylistRepository.findFirstByPlaylistOrderByCreatedAt(playlist).getPlaylist().getImagePath();
        }
    }

}
