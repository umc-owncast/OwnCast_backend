package com.umc.owncast.domain.cast.service;

import com.umc.owncast.common.exception.handler.UserHandler;
import com.umc.owncast.common.response.status.ErrorCode;
import com.umc.owncast.domain.cast.dto.*;
import com.umc.owncast.domain.cast.entity.Cast;
import com.umc.owncast.domain.cast.repository.CastRepository;
import com.umc.owncast.domain.cast.service.chatGPT.script.ScriptService;
import com.umc.owncast.domain.castplaylist.entity.CastPlaylist;
import com.umc.owncast.domain.castplaylist.repository.CastPlaylistRepository;
import com.umc.owncast.domain.memberprefer.entity.MainPrefer;
import com.umc.owncast.domain.memberprefer.repository.MemberPreferRepository;
import com.umc.owncast.domain.playlist.entity.Playlist;
import com.umc.owncast.domain.playlist.repository.PlaylistRepository;
import com.umc.owncast.domain.sentence.dto.SentenceResponseDTO;
import com.umc.owncast.domain.sentence.entity.Sentence;
import com.umc.owncast.domain.sentence.service.SentenceService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CastServiceImpl implements CastService {
    private final ScriptService scriptService;
    private final TTSService ttsService;
    private final FileService fileService;
    private final SentenceService sentenceService;

    private final CastRepository castRepository;
    private final PlaylistRepository playlistRepository;
    private final CastPlaylistRepository castPlaylistRepository;
    private final MemberPreferRepository memberPreferRepository;

    @Override
    public CastScriptDTO createCastByKeyword(KeywordCastCreationDTO castRequest) {
        String script = scriptService.createScript(castRequest);
        return handleCastCreation(castRequest, script);
    }

    @Override
    public CastScriptDTO createCastByScript(ScriptCastCreationDTO castRequest) {
        String script = castRequest.getScript().strip();
        KeywordCastCreationDTO request = KeywordCastCreationDTO.builder()
                .voice(castRequest.getVoice())
                .formality(castRequest.getFormality())
                .build();
        return handleCastCreation(request, script);
    }

    /** Cast와 Sentence 저장 후 CastScriptDTO로 묶어 반환 */
    private CastScriptDTO handleCastCreation(KeywordCastCreationDTO castRequest, String script) {
        TTSResultDTO ttsResult = ttsService.createSpeech(script, castRequest);
        Double audioLength = ttsResult.getTimePointList().get(ttsResult.getTimePointList().size() - 1);
        int minutes = (int) (audioLength / 60);
        int seconds = (int) Math.round(audioLength % 60);

        Cast cast = Cast.builder()
                .voice(castRequest.getVoice())
                .audioLength(String.format("%02d:%02d", minutes, seconds))
                .filePath(ttsResult.getMp3Path())
                .formality(castRequest.getFormality())
                .member(null) // TODO 회원 기능 만들어지면 자기자신 넣기
                .language(null) // TODO 회원 기능 만들어지면 언어 설정 넣기
                .isPublic(false)
                .hits(0L)
                .build();
        cast = castRepository.save(cast);
        List<Sentence> sentences = sentenceService.save(script, ttsResult, cast);

        return CastScriptDTO.builder()
                .id(cast.getId())
                .fileUrl(cast.getFilePath())
                .sentences(sentences.stream()
                        .map(SentenceResponseDTO::new)
                        .toList())
                .build();
    }

    @Override
    public SimpleCastDTO saveCast(Long castId, CastUpdateRequestDTO saveRequest) {
        return updateCast(castId, saveRequest);
    }
    
    /** Cast 커버 이미지 교체 */
    private CastUpdateDTO setCastImage(Cast cast, CastUpdateRequestDTO updateRequest) {
        if (updateRequest.getImage() == null) return null;
        String imagePath = fileService.uploadFile(updateRequest.getImage());
        fileService.deleteFile(cast.getImagePath());
        return CastUpdateDTO.builder()
                .title(updateRequest.getTitle())
                .imagePath(imagePath)
                .isPublic(updateRequest.getIsPublic())
                .playlistId(updateRequest.getPlaylistId())
                .build();
    }

    @Override
    public SimpleCastDTO updateCast(Long castId, CastUpdateRequestDTO updateRequest) {
        Cast cast = castRepository.findById(castId).orElseThrow(() -> new NoSuchElementException("캐스트가 존재하지 않습니다"));
        // 이미지 설정
        CastUpdateDTO updateDTO = setCastImage(cast, updateRequest);
        // 플레이리스트 변경
        changePlaylist(cast, updateRequest.getPlaylistId());
        // cast 필드 수정
        cast.update(updateDTO);
        castRepository.save(cast);

        return new SimpleCastDTO(cast);
    }

    /** 캐스트가 속한 플레이리스트 변경 */
    private void changePlaylist(Cast cast, Long playlistId){
        if(cast == null || playlistId == null) return;
        Optional<CastPlaylist> optionalOldCp = castPlaylistRepository.findByPlaylistMemberIdAndCastId(1L, cast.getId()); // TODO 회원 기능 연동
        if(optionalOldCp.isPresent()){
            CastPlaylist oldCp = optionalOldCp.get();
            if(Objects.equals(oldCp.getPlaylist().getId(), playlistId)) return; // 동일한 플레이리스트인 경우 스킵
            else {
                castPlaylistRepository.delete(oldCp); // 이전 castPlaylist 삭제
            }
        }
        Playlist playlist = playlistRepository.findById(playlistId).orElseThrow(() -> new UserHandler(ErrorCode.PLAYLIST_NOT_FOUND));
        castPlaylistRepository.save(CastPlaylist.builder() // 새 castPlaylist 추가
                .cast(cast)
                .playlist(playlist)
                .build());
    }

    @Override
    public ResponseEntity<UrlResource> streamCast(Long castId, HttpHeaders headers) {
        try {
            Cast cast = castRepository.findById(castId).orElseThrow(() -> new NoSuchElementException("캐스트가 존재하지 않습니다."));
            cast.updateHits();
            return fileService.streamFile(cast.getFilePath(), headers);
        } catch (IOException e) {
            System.out.println("CastServiceImpl: IOException at stream() -> " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("캐스트 스트리밍에 실패하였습니다.");
        }
    }

    @Override
    public CastDTO findCast(Long castId) {
        Cast cast = castRepository.findById(castId).orElseThrow(() -> new NoSuchElementException("캐스트가 존재하지 않습니다"));
        return new CastDTO(cast);
    }

    @Override
    public SimpleCastDTO deleteCast(Long castId) {
        Cast cast = castRepository.findById(castId).orElseThrow(() -> new NoSuchElementException("캐스트가 존재하지 않습니다"));
        castRepository.delete(cast);
        return new SimpleCastDTO(cast);
    }

    @Override
    public MainPrefer getMemberPrefer(Long memberId) {
        return memberPreferRepository.findByMemberId(memberId)
                .orElseThrow(() -> new UserHandler(ErrorCode.CAST_NOT_FOUND));

    }

    @Override
    public List<CastHomeDTO> getHomeCast() {

        final int TOP_CASTS_LIMIT = 5;

        MainPrefer userMainCategory = getMemberPrefer(1L);
        Long userCategoryId = userMainCategory.getMainCategory().getId();
        Pageable pageable = PageRequest.of(0, TOP_CASTS_LIMIT);
        List<Cast> castMainCategories = castRepository.findTop5ByMainCategoryIdOrderByHitsDesc(userCategoryId, 1L, pageable).getContent();

        return convertToCastHomeDTO(castMainCategories);
    }

    @Override
    public List<CastHomeDTO> getCast(String keyword) {
        List<Cast> castList = castRepository.castSearch(keyword, 1L);

        return convertToCastHomeDTO(castList);
    }

    @Override
    public OtherCastResponseDTO getOtherCast(OtherCastRequestDTO castSaveRequestDTO) {

        Playlist playlist = playlistRepository.findById(castSaveRequestDTO.getPlaylistId())
                .orElseThrow(() -> new UserHandler(ErrorCode.PLAYLIST_NOT_FOUND));

        Cast cast = castRepository.findById(castSaveRequestDTO.getCastId())
                .orElseThrow(() -> new UserHandler(ErrorCode.CAST_NOT_FOUND));

        if (castPlaylistRepository.existsByMemberIdAndCastId(1L, cast.getId())) { // 해당 캐스트가 유저의 플레이리스트에 이미 저장한 경우
            throw new UserHandler(ErrorCode.CAST_ALREADY_EXIST);
        }

        if (!cast.getIsPublic()) {
            throw new UserHandler(ErrorCode.CAST_PRIVATE);
        }

        // 새로운 CastPlaylist 객체를 생성하고 저장
        CastPlaylist newCastPlaylist = castPlaylistRepository.save(
                CastPlaylist.builder()
                        .cast(cast)
                        .playlist(playlist)
                        .build()
        );

        return OtherCastResponseDTO.builder()
                .castPlaylistId(newCastPlaylist.getId())
                .memberId(1L)
                .build();
    }

    private List<CastHomeDTO> convertToCastHomeDTO(List<Cast> castList) {

        return castList.stream().map(cast -> {

            String castPlaylistName = playlistRepository.findUserCategoryName(cast.getId());

            if (castPlaylistName == null) {
                throw new UserHandler(ErrorCode.CAST_PLAYLIST_NOT_FOUND);
            }

            return CastHomeDTO.builder()
                    .id(cast.getId())
                    .title(cast.getTitle())
                    .imagePath(cast.getImagePath())
                    .memberName(cast.getMember().getUsername())
                    .audioLength(cast.getAudioLength())
                    .playlistName(castPlaylistName)
                    .build();
        }).toList();
    }


}
