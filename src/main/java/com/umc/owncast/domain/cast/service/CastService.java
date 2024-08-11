package com.umc.owncast.domain.cast.service;

import com.umc.owncast.common.response.ApiResponse;
import com.umc.owncast.common.response.status.SuccessCode;
import com.umc.owncast.domain.cast.dto.*;
import com.umc.owncast.domain.cast.entity.Cast;
import com.umc.owncast.domain.cast.repository.CastRepository;
import com.umc.owncast.domain.castplaylist.entity.CastPlaylist;
import com.umc.owncast.domain.castplaylist.repository.CastPlaylistRepository;
import com.umc.owncast.domain.playlist.entity.Playlist;
import com.umc.owncast.domain.playlist.repository.PlaylistRepository;
import com.umc.owncast.domain.sentence.dto.SentenceResponseDTO;
import com.umc.owncast.domain.sentence.entity.Sentence;
import com.umc.owncast.domain.sentence.service.SentenceService;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class CastService {
    private final ScriptService scriptService;
    private final TTSService ttsService;
    private final StreamService streamService;
    private final FileService fileService;
    private final SentenceService sentenceService;

    private final CastRepository castRepository;
    private final PlaylistRepository playlistRepository;
    private final CastPlaylistRepository castPlaylistRepository;

    /**
     * keyword로 cast 생성
     *
     * @param castRequest cast 관련 정보
     * @return cast를 List&lt;Sentence>에 담아 반환
     */
    public ApiResponse<Object> createCastByKeyword(KeywordCastCreationDTO castRequest) {
        String script = scriptService.createScript(castRequest);
        return getObjectApiResponse(castRequest, script);
    }

    /**
     * script로 cast 생성
     *
     * @param castRequest cast 관련 정보
     * @return cast를 List&lt;Sentence>에 담아 반환
     */
    public ApiResponse<Object> createCastByScript(ScriptCastCreationDTO castRequest) {
        String script = castRequest.getScript();
        KeywordCastCreationDTO request = KeywordCastCreationDTO.builder()
                .voice(castRequest.getVoice())
                .formality(castRequest.getFormality())
                .build();
        return getObjectApiResponse(request, script);
    }

    private @NotNull ApiResponse<Object> getObjectApiResponse(KeywordCastCreationDTO castRequest, String script) {
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

        CastScriptDTO response = new CastScriptDTO(cast); // TODO 여기서 return + 함수명 createCastResponse 등으로 변경
        response.setSentences(sentences.stream()
                .map(SentenceResponseDTO::new)
                .toList());
        return ApiResponse.of(SuccessCode._OK, response);
    }

    public ApiResponse<Object> saveCast(Long castId, CastSaveDTO saveRequest, MultipartFile image) {
        // 제목, 커버이미지, 공개여부 등 저장
        updateCast(castId,
                CastUpdateDTO.builder()
                        .title(saveRequest.getTitle())
                        .isPublic(saveRequest.getIsPublic())
                        .build(),
                image
        );
        // 플레이리스트 저장
        Playlist playlist = playlistRepository.findById(saveRequest.getPlaylistId()).orElseThrow(() -> new NoSuchElementException("플레이리스트가 존재하지 않습니다."));
        Cast cast = castRepository.findById(castId).orElseThrow(() -> new NoSuchElementException("캐스트가 존재하지 않습니다."));
        CastPlaylist castPlaylist = CastPlaylist.builder()
                .cast(cast)
                .playlist(playlist)
                .build();
        castPlaylistRepository.findByCastIdAndPlaylistId(castId, playlist.getId())
                .ifPresent(cp -> {
                    throw new RuntimeException("캐스트가 이미 해당 플레이리스트에 저장되어 있습니다.");
                });

        castPlaylistRepository.save(castPlaylist);
        return ApiResponse.of(SuccessCode._OK, "저장되었습니다.");
    }

    private void setCastImage(Cast cast, CastUpdateDTO updateRequest, MultipartFile image) {
        if (image == null) return;
        String imagePath = fileService.uploadFile(image);
        fileService.deleteFile(cast.getImagePath());
        updateRequest.setImagePath(imagePath);
    }

    public ApiResponse<Object> updateCast(Long castId, CastUpdateDTO updateRequest, MultipartFile image) {
        Cast cast = castRepository.findById(castId).orElseThrow(() -> new NoSuchElementException("캐스트가 존재하지 않습니다"));
        setCastImage(cast, updateRequest, image);
        cast.update(updateRequest);
        castRepository.save(cast);

        return ApiResponse.of(SuccessCode._OK, "수정되었습니다.");
    }

    public ResponseEntity<UrlResource> streamCast(Long castId, HttpHeaders headers) {
        try {
            Cast cast = castRepository.findById(castId).orElseThrow(() -> new NoSuchElementException("캐스트가 존재하지 않습니다."));
            cast.updateHits();
            return streamService.stream(castId, headers);
        } catch (IOException e) {
            System.out.println("CastService: IOException at stream() -> " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("캐스트 스트리밍에 실패하였습니다.");
        }
    }

    public ApiResponse<Object> fetchCastScript(Long castId) {
        List<Sentence> sentences = sentenceService.findCastSentence(castId);
        List<SentenceResponseDTO> response = sentences.stream()
                .map(SentenceResponseDTO::new)
                .toList();
        return ApiResponse.of(SuccessCode._OK, response);
    }

    public ApiResponse<Object> deleteCast(Long castId) {
        Cast cast = castRepository.findById(castId).orElseThrow(() -> new NoSuchElementException("캐스트가 존재하지 않습니다"));
        castRepository.delete(cast);
        return ApiResponse.of(SuccessCode._OK, cast);
    }
}
