package com.umc.owncast.domain.cast.service;

import com.umc.owncast.domain.cast.dto.*;
import com.umc.owncast.domain.cast.entity.Cast;
import com.umc.owncast.domain.memberprefer.entity.MainPrefer;
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
    Cast saveCast(Long castId, CastUpdateDTO saveRequest, MultipartFile image);

    /** Cast 수정 */
    Cast updateCast(Long castId, CastUpdateRequestDTO updateRequest);

    /** Cast 오디오파일 스트리밍 */
    ResponseEntity<UrlResource> streamCast(Long castId, HttpHeaders headers);

    /** Cast 스크립트 검색 */
    CastDTO findCast(Long castId);

    SimpleCastDTO deleteCast(Long castId);

    /** 사용자의 관심사 가져오기 */
    MainPrefer getMemberPrefer(Long memberId);

    /** 사용자의 관심사 설정에 따른 Cast 추천 */
    List<CastHomeDTO> getHomeCast();

    /** 캐스트 검색 */
    List<CastHomeDTO> getCast(String keyword);

    /** 다른 사람의 캐스트 담아오기 */
    OtherCastResponseDTO getOtherCast(OtherCastRequestDTO castSaveRequestDTO);
}
