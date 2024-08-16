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
                .sentences(sentences.stream()
                        .map(SentenceResponseDTO::new)
                        .toList())
                .build();
    }

    @Override
    public String saveCast(Long castId, CastSaveDTO saveRequest, MultipartFile image) {
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
        return "저장되었습니다.";
    }
    
    /** Cast 커버 이미지 수정 */
    private void setCastImage(Cast cast, CastUpdateDTO updateRequest, MultipartFile image) {
        if (image == null) return;
        String imagePath = fileService.uploadFile(image);
        fileService.deleteFile(cast.getImagePath());
        updateRequest.setImagePath(imagePath);
    }

    @Override
    public String updateCast(Long castId, CastUpdateDTO updateRequest, MultipartFile image) {
        Cast cast = castRepository.findById(castId).orElseThrow(() -> new NoSuchElementException("캐스트가 존재하지 않습니다"));
        setCastImage(cast, updateRequest, image);
        cast.update(updateRequest);
        castRepository.save(cast);

        return "수정되었습니다.";
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
    public List<SentenceResponseDTO> fetchCastScript(Long castId) {
        List<Sentence> sentences = sentenceService.findCastSentence(castId);
        return sentences.stream()
                .map(SentenceResponseDTO::new)
                .toList();
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
                    .memberName(cast.getMember().getUsername())
                    .audioLength(cast.getAudioLength())
                    .playlistName(castPlaylistName)
                    .build();
        }).toList();
    }


}
