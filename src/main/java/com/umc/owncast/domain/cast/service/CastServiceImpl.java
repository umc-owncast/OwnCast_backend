package com.umc.owncast.domain.cast.service;

import com.umc.owncast.common.exception.handler.UserHandler;
import com.umc.owncast.common.response.status.ErrorCode;
import com.umc.owncast.domain.cast.dto.*;
import com.umc.owncast.domain.cast.entity.Cast;
import com.umc.owncast.domain.cast.repository.CastRepository;
import com.umc.owncast.domain.cast.service.chatGPT.script.ScriptService;
import com.umc.owncast.domain.castplaylist.entity.CastPlaylist;
import com.umc.owncast.domain.castplaylist.repository.CastPlaylistRepository;
import com.umc.owncast.domain.enums.Language;
import com.umc.owncast.domain.enums.MainCategory;
import com.umc.owncast.domain.member.entity.Member;
import com.umc.owncast.domain.memberprefer.entity.SubPrefer;
import com.umc.owncast.domain.memberprefer.repository.SubPreferRepository;
import com.umc.owncast.domain.playlist.entity.Playlist;
import com.umc.owncast.domain.playlist.repository.PlaylistRepository;
import com.umc.owncast.domain.sentence.dto.SentenceResponseDTO;
import com.umc.owncast.domain.sentence.entity.Sentence;
import com.umc.owncast.domain.sentence.service.SentenceService;
import com.umc.owncast.domain.sentence.service.TranslationService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicReference;

@Service
@RequiredArgsConstructor
public class CastServiceImpl implements CastService {
    private final ScriptService scriptService;
    private final TTSService ttsService;
    private final FileService fileService;
    private final SentenceService sentenceService;
    //private final ChatGptScriptDivider scriptDivider;
    private final ParsingService parsingService;
    private final TranslationService translationService;

    private final CastRepository castRepository;
    private final PlaylistRepository playlistRepository;
    private final CastPlaylistRepository castPlaylistRepository;
    private final SubPreferRepository subPreferRepository;

    @Qualifier("DefaultExecutor")
    private final ThreadPoolTaskExecutor executor;



    @Value("${app.image.default-path}")
    private String CAST_DEFAULT_IMAGE_PATH;

    @Override
    public CastScriptDTO createCastByKeyword(KeywordCastCreationDTO castRequest, Member member) {
        String script = scriptService.createScript(member, castRequest);
        //String dividedScript = scriptDivider.placeDelimiter(script, "@");
        String dividedScript = parsingService.placeDelimiter(script);
//        return handleCastCreation(castRequest, script, member);
        return handleCastCreation(castRequest, dividedScript, member);
    }

    @Override
    public CastScriptDTO createCastByScript(ScriptCastCreationDTO castRequest, Member member) {
        //String script = scriptDivider.placeDelimiter(castRequest.getScript(), "@");
        String script = parsingService.placeDelimiter(castRequest.getScript());
        KeywordCastCreationDTO request = KeywordCastCreationDTO.builder()
                .voice(castRequest.getVoice())
                .formality(castRequest.getFormality())
                .build();
        return handleCastCreation(request, script, member);
    }

    /** Cast와 Sentence 저장 후 CastScriptDTO로 묶어 반환 */
    private CastScriptDTO handleCastCreation(KeywordCastCreationDTO castRequest, String script, Member member) {
        AtomicReference<String[]> seperatedScriptReference = new AtomicReference<>();
        AtomicReference<TTSResultDTO> ttsResultReference = new AtomicReference<>();
        AtomicReference<Cast> castReference = new AtomicReference<>();

        // 영문 스크립트 분리 / TTS 요청 / Cast 저장
        CompletableFuture<Cast> castFuture = CompletableFuture.supplyAsync(
            () -> parsingService.parseSentencesByDelimiter(script),
            executor
        ).thenCompose((String[] seperated) -> {
            seperatedScriptReference.set(seperated);
            return CompletableFuture.supplyAsync(() -> ttsService.createSpeech(seperated, castRequest), executor);
        }).thenCompose((TTSResultDTO ttsResult) -> {
            ttsResultReference.set(ttsResult);
            Double audioLength = ttsResult.getTimePointList().get(ttsResult.getTimePointList().size() - 1);
            int minutes = (int) (audioLength / 60);
            int seconds = (int) Math.round(audioLength % 60);
            Cast cast = Cast.builder()
                   .voice(castRequest.getVoice())
                   .audioLength(String.format("%02d:%02d", minutes, seconds))
                   .filePath(ttsResult.getMp3Path())
                   .formality(castRequest.getFormality())
                   .imagePath(CAST_DEFAULT_IMAGE_PATH)
                   .member(member)
                   .language(member.getLanguage())
                   .isPublic(false)
                   .hits(0L)
                   .build();
            return CompletableFuture.supplyAsync(() -> castRepository.save(cast), executor);
        });

        // 영문 스크립트 번역 & 문장 별 분리
        CompletableFuture<String[]> translationFuture = CompletableFuture.supplyAsync(
            () -> translationService.translateToKorean(script),
            executor
        ).thenCompose((String translatedScript) ->
            CompletableFuture.supplyAsync(() -> parsingService.parseSentencesByDelimiter(translatedScript), executor)
        );

        // 생성된 Sentence 저장
        List<Sentence> savedSentences = castFuture.thenCombine(translationFuture, (Cast cast, String[] parsedKoreanScript) -> {
            castReference.set(cast);
            return sentenceService.saveSentences(seperatedScriptReference.get(), parsedKoreanScript, ttsResultReference.get(), cast);
        }).join();

        Cast cast = castReference.get();
        return CastScriptDTO.builder()
                .id(cast.getId())
                .fileUrl(cast.getFilePath())
                .sentences(savedSentences.stream()
                        .map(SentenceResponseDTO::new)
                        .toList())
                .build();

    }

    @Override
    public SimpleCastDTO saveCast(Long castId, CastUpdateRequestDTO saveRequest, Member member, MultipartFile image) {
        return updateCast(castId, saveRequest, member, image);
    }
    
    /** Cast 커버 이미지 교체 */
    private CastUpdateDTO setCastImage(Cast cast, CastUpdateRequestDTO updateRequest, MultipartFile image) {
        String imagePath = fileService.uploadFile(image);
        fileService.deleteFile(cast.getImagePath());
        return CastUpdateDTO.builder()
                .title(updateRequest.getTitle())
                .imagePath(imagePath)
                .isPublic(updateRequest.getIsPublic())
                .playlistId(updateRequest.getPlaylistId())
                .build();
    }

    @Override
    public SimpleCastDTO updateCast(Long castId, CastUpdateRequestDTO updateRequest, Member member, MultipartFile image) {
        Cast cast = castRepository.findById(castId).orElseThrow(() -> new NoSuchElementException("캐스트가 존재하지 않습니다"));
        if(!Objects.equals(cast.getMember().getId(), member.getId())){
            throw new UserHandler(ErrorCode.NO_AUTHORITY);
        }
        // 이미지 설정
        CastUpdateDTO updateDTO = setCastImage(cast, updateRequest, image);
        // 플레이리스트 변경
        changePlaylist(member, cast, updateRequest.getPlaylistId());
        // cast 필드 수정
        cast.update(updateDTO);
        castRepository.save(cast);

        return new SimpleCastDTO(cast);
        /*setCastImage(cast, updateRequest, image);
        cast.update(updateRequest);
        castRepository.save(cast);

        return cast;
        */
    }

    /** 캐스트가 속한 플레이리스트 변경 */
    private void changePlaylist(Member member, Cast cast, Long playlistId){
        
        if(cast == null || playlistId == null) return;
        Optional<CastPlaylist> optionalOldCp = castPlaylistRepository.findByPlaylistMemberIdAndCastId(member.getId(), cast.getId());
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
    public CastDTO findCast(Long castId, Member member) {
        Cast cast = castRepository.findById(castId).orElseThrow(() -> new NoSuchElementException("캐스트가 존재하지 않습니다"));
        if(cast.getIsPublic().equals(false) && !Objects.equals(cast.getMember().getId(), member.getId())){
            // 비공개인데 제작자 외의 회원이 접근하는 경우 예외 처리
            throw new UserHandler(ErrorCode.NO_AUTHORITY);
        }
        return new CastDTO(cast);
    }

    @Override
    @Transactional
    public SimpleCastDTO deleteCast(Long castId, Member member) {
        Cast cast = castRepository.findById(castId).orElseThrow(() -> new NoSuchElementException("캐스트가 존재하지 않습니다"));
        if(!Objects.equals(cast.getMember(), member)){
            throw new UserHandler(ErrorCode.NO_AUTHORITY);
        }
        if(!cast.getImagePath().equals(CAST_DEFAULT_IMAGE_PATH)) {
            fileService.deleteFile(cast.getImagePath());
        }
        fileService.deleteFile(cast.getFilePath());

        castPlaylistRepository.deleteAllByCast(cast);
        sentenceService.deleteAllByCast(cast);

        castRepository.delete(cast);
        return new SimpleCastDTO(cast);
    }

    private MainCategory getMemberPrefer(Member member) {
        SubPrefer subPrefer = subPreferRepository.findByMember(member).orElseThrow(() -> new UserHandler(ErrorCode.CAST_NOT_FOUND));
        return subPrefer.getSubCategory().getMainCategory();
    }

    @Override
    public List<CastHomeDTO> getHomeCast(Member member) {

        final int TOP_CASTS_LIMIT = 5;
        Pageable pageable = PageRequest.of(0, TOP_CASTS_LIMIT);

        MainCategory userCategory = getMemberPrefer(member);
        Language userLanguage = member.getLanguage();

        List<Cast> castMainCategories = castRepository.findTop5ByMainCategoryOrderByHitsDesc(userCategory, member, userLanguage, pageable).getContent();

        return convertToCastHomeDTO(castMainCategories);
    }

    @Override
    public List<CastHomeDTO> getCast(Member member, String keyword) {

        List<Cast> castList = castRepository.castSearch(keyword, member.getId(), member.getLanguage().name());
        return convertToCastHomeDTO(castList);
    }

    @Override
    public OtherCastResponseDTO getOtherCast(Member member, OtherCastRequestDTO castSaveRequestDTO) {

        Playlist playlist = playlistRepository.findById(castSaveRequestDTO.getPlaylistId())
                .orElseThrow(() -> new UserHandler(ErrorCode.PLAYLIST_NOT_FOUND));

        Cast cast = castRepository.findById(castSaveRequestDTO.getCastId())
                .orElseThrow(() -> new UserHandler(ErrorCode.CAST_NOT_FOUND));

        if (castPlaylistRepository.existsByMemberIdAndCastId(member.getId(), cast.getId())) { // 해당 캐스트가 유저의 플레이리스트에 이미 저장한 경우
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
                .memberId(member.getId())
                .build();
    }

    private List<CastHomeDTO> convertToCastHomeDTO(List<Cast> castList) {

        return castList.stream().map(cast -> {

            System.out.println(cast.getId());

            String castPlaylistName = castPlaylistRepository.findUserCategoryName(cast.getId());

            if (castPlaylistName == null) {
                throw new UserHandler(ErrorCode.CAST_PLAYLIST_NOT_FOUND);
            }

            return CastHomeDTO.builder()
                    .id(cast.getId())
                    .title(cast.getTitle())
                    .imagePath(cast.getImagePath())
                    .memberName(cast.getMember().getNickname())
                    .audioLength(cast.getAudioLength())
                    .playlistName(castPlaylistName)
                    .build();
        }).toList();
    }


}
