package com.umc.owncast.domain.cast.service;

import com.umc.owncast.domain.cast.dto.*;
import com.umc.owncast.domain.sentence.dto.SentenceResponseDTO;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CastService {

    /** keyword로 cast 생성 */
    CastScriptDTO createCastByKeyword(KeywordCastCreationDTO castRequest);

    /** script로 cast 생성 */
    CastScriptDTO createCastByScript(ScriptCastCreationDTO castRequest);

    /** Cast 제목, 커버이미지, 공개여부, 플레이리스트 저장 */
    String saveCast(Long castId, CastSaveDTO saveRequest, MultipartFile image);

    /** Cast 수정 */
    String updateCast(Long castId, CastUpdateDTO updateRequest, MultipartFile image);

    /** Cast 오디오파일 스트리밍 */
    ResponseEntity<UrlResource> streamCast(Long castId, HttpHeaders headers);

    /** Cast 스크립트 검색 */
    List<SentenceResponseDTO> fetchCastScript(Long castId);

    SimpleCastDTO deleteCast(Long castId);

    /** 홈 화면에서 보여줄 캐스트 검색?? TODO 바꿔줘 */
    List<CastHomeDTO> getHomeCast();

    List<CastHomeDTO> getCast(String keyword);

    Long getOtherCast(OtherCastRequestDTO castSaveRequestDTO);
}
