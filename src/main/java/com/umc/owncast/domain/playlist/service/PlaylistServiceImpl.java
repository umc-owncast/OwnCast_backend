package com.umc.owncast.domain.playlist.service;

import com.umc.owncast.common.annotation.TrackExecutionTime;
import com.umc.owncast.common.exception.handler.UserHandler;
import com.umc.owncast.common.response.status.ErrorCode;
import com.umc.owncast.domain.cast.entity.Cast;
import com.umc.owncast.domain.cast.repository.CastRepository;
import com.umc.owncast.domain.castplaylist.repository.CastPlaylistRepository;
import com.umc.owncast.domain.member.entity.Member;
import com.umc.owncast.domain.playlist.dto.*;
import com.umc.owncast.domain.playlist.entity.Playlist;
import com.umc.owncast.domain.playlist.factory.PlaylistDTOFactory;
import com.umc.owncast.domain.playlist.repository.PlaylistRepository;
import com.umc.owncast.domain.playlist.template.playlist.GetAllMyPlaylist;
import com.umc.owncast.domain.playlist.template.playlist.GetPlaylistById;
import com.umc.owncast.domain.playlist.template.playlist.GetPlaylists;
import com.umc.owncast.domain.playlist.template.playlist.GetSavedPlaylist;
import com.umc.owncast.domain.playlist.template.crud.CreatePlaylist;
import com.umc.owncast.domain.playlist.template.crud.DeletePlaylist;
import com.umc.owncast.domain.playlist.template.crud.PlaylistCRUD;
import com.umc.owncast.domain.playlist.template.crud.UpdatePlaylist;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
@Service
public class PlaylistServiceImpl implements PlaylistService {

    private final PlaylistRepository playlistRepository;
    private final CastRepository castRepository;
    private final CastPlaylistRepository castPlaylistRepository;
    /*private final RedisSingleDataService redisSingleDataService;*/
    private GetPlaylists getPlaylists;
    private PlaylistCRUD playlistCRUD;

    @Value("${app.image.default-path}")
    private String DEFAULT_IMAGE_PATH;

    @Override
    @Transactional
    public CreatePlaylistDTO addPlaylist(Member member, String playlistName) {

        playlistCRUD = new CreatePlaylist(playlistRepository);

        // TODO 이렇게 타입 캐스팅 해도 될까?
        return (CreatePlaylistDTO) playlistCRUD.execute(member, playlistName, 0);
    }

    @Override
    @Transactional
    public DeletePlaylistDTO deletePlaylist(Member member, Long playlistId) {

        //TODO 주입 받는게 마음에 안듬. 강한 결합?!
        playlistCRUD = new DeletePlaylist(playlistRepository);

        return (DeletePlaylistDTO) playlistCRUD.execute(member, null, playlistId);

    }

    @Override
    @Transactional
    public UpdatePlaylistDTO updatePlaylist(Member member, Long playlistId, String playlistName) {

        playlistCRUD = new UpdatePlaylist(playlistRepository);

        return (UpdatePlaylistDTO) playlistCRUD.execute(member, playlistName, playlistId);
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

        getPlaylists = new GetSavedPlaylist(castPlaylistRepository, playlistRepository);

        return getPlaylists.get(member, 0, page);
    }

    @Override
    public List<CastDTO> getAllMyPlaylists(Member member, int page) {
        getPlaylists = new GetAllMyPlaylist(castPlaylistRepository, playlistRepository);

        return getPlaylists.get(member, 0, page);
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

        /*redisSingleDataService.setSingleData(String.valueOf(playlistId), null);*/

        return DeleteCastFromPlaylistDTO.builder()
                .castId(castId)
                .build();
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
