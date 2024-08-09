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
import com.umc.owncast.domain.sentence.repository.SentenceRepository;
import com.umc.owncast.domain.sentence.service.SentenceService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class CastService {
    private final ScriptService scriptService;
    private final TranslateService translateService;
    private final TTSService ttsService;
    private final StreamService streamService;
    private final FileService fileService;
    private final SentenceService sentenceService; // 가칭

    private final CastRepository castRepository;
    private final SentenceRepository sentenceRepository;
    private final PlaylistRepository playlistRepository;
    private final CastPlaylistRepository castPlaylistRepository;

    /**
     * keyword로 cast 생성
     * @param castRequest cast 관련 정보
     * @return cast를 List&lt;Sentence>에 담아 반환
     */
    public ApiResponse<Object> createCast(KeywordCastCreationDTO castRequest){
        String script = scriptService.createScript(castRequest);
        TTSResultDTO ttsResult = ttsService.createSpeech(script, castRequest);
        Cast cast = Cast.builder()
                .voice(castRequest.getVoice())
                .audioLength(castRequest.getAudioTime()) // TODO mp3 파일 길이 가져오기
                .filePath(ttsResult.getMp3Path())
                .formality(castRequest.getFormality())
                .member(null) // TODO 회원 기능 만들어지면 자기자신 넣기
                .language(null) // TODO 회원 기능 만들어지면 언어 설정 넣기
                .isPublic(false)
                .hits(0L)
                .build();
        List<Sentence> sentences = sentenceService.mapToSentence(
                script,
                translateService.translate(script),
                ttsResult,
                cast
        );
        cast.addSentences(sentences);
        castRepository.save(cast);
        sentenceRepository.saveAll(sentences);

        SimpleCastResponseDTO response = new SimpleCastResponseDTO(cast);
        return ApiResponse.of(SuccessCode._OK, response);
    }

    /**
     * script로 cast 생성
     * @param castRequest cast 관련 정보
     * @return cast를 List&lt;Sentence>에 담아 반환
     */
    public ApiResponse<Object> createCast(ScriptCastCreationDTO castRequest){
        String script = castRequest.getScript();
        TTSResultDTO ttsResult = ttsService.createSpeech(script, KeywordCastCreationDTO.builder()
                        .voice(castRequest.getVoice())
                        .formality(castRequest.getFormality())
                        .build());
        Cast cast = Cast.builder()
                .voice(castRequest.getVoice())
                .audioLength(0) // TODO mp3 파일 길이 가져오기
                .filePath(ttsResult.getMp3Path())
                .formality(castRequest.getFormality())
                .member(null) // TODO 회원 기능 만들어지면 자기자신 넣기
                .language(null) // TODO 회원 기능 만들어지면 언어 설정 넣기
                .isPublic(false)
                .hits(0L)
                .build();
        List<Sentence> sentences = sentenceService.mapToSentence(
                script,
                translateService.translate(script),
                ttsResult,
                cast
        );
        cast.addSentences(sentences);
        castRepository.save(cast);
        sentenceRepository.saveAll(sentences);

        SimpleCastResponseDTO response = new SimpleCastResponseDTO(cast);
        return ApiResponse.of(SuccessCode._OK, response);
    }

    public ApiResponse<Object> saveCast(Long castId, CastSaveDTO saveRequest){
        // 제목, 커버이미지, 공개여부 등 저장
        updateCast(castId, CastUpdateDTO.builder()
                .title(saveRequest.getTitle())
                .castImage(saveRequest.getCastImage())
                .isPublic(saveRequest.getIsPublic())
                .imagePath(saveRequest.getImagePath())
                .build()
        );
        // 플레이리스트 저장
        Playlist playlist = playlistRepository.findById(saveRequest.getPlaylistId()).orElseThrow(() -> new NoSuchElementException("플레이리스트가 존재하지 않습니다."));
        Cast cast = castRepository.findById(castId).orElseThrow(() -> new NoSuchElementException("캐스트가 존재하지 않습니다."));
        CastPlaylist castPlaylist = CastPlaylist.builder()
                .cast(cast)
                .playlist(playlist)
                .build();
        castPlaylistRepository.save(castPlaylist);
        return ApiResponse.of(SuccessCode._OK, "저장되었습니다.");
    }

    public ApiResponse<Object> updateCast(Long castId, CastUpdateDTO updateRequest) {
        Cast target = castRepository.findById(castId).orElseThrow(() -> new NoSuchElementException("캐스트가 존재하지 않습니다"));
        // TODO 이미지
        // - 원래 이미지 삭제
        // - 현재 이미지명 난수화 -> 필요?
        // - 현재 이미지 저장 O
        String imagePath = fileService.uploadFile(updateRequest.getCastImage());
        updateRequest.setImagePath(imagePath);
        target.update(updateRequest);
        castRepository.save(target);

        return ApiResponse.of(SuccessCode._OK, target);
    }

    public ResponseEntity<UrlResource> streamCast(Long castId, HttpHeaders headers) {
        try {
            Cast cast = castRepository.findById(castId).orElseThrow(() -> new NoSuchElementException("캐스트가 존재하지 않습니다."));
            cast.updateHits();
            return streamService.stream(castId, headers);
        } catch (IOException e){
            System.out.println("CastService: IOException at stream() -> " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("캐스트 스트리밍에 실패하였습니다.");
        }
    }
}
