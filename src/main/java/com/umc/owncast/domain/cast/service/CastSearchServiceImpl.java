package com.umc.owncast.domain.cast.service;

import com.umc.owncast.common.exception.handler.UserHandler;
import com.umc.owncast.common.response.status.ErrorCode;
import com.umc.owncast.domain.cast.dto.CastDTO;
import com.umc.owncast.domain.cast.entity.Cast;
import com.umc.owncast.domain.cast.repository.CastRepository;
import com.umc.owncast.domain.memberprefer.entity.MainPrefer;
import com.umc.owncast.domain.memberprefer.repository.MemberPreferRepository;
import com.umc.owncast.domain.playlist.repository.PlaylistRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class CastSearchServiceImpl implements CastSearchService{
    private final CastRepository castRepository;
    private final MemberPreferRepository memberPreferRepository;
    private final PlaylistRepository playlistRepository;

    @Override
    public List<CastDTO.CastHomeDTO> getHomeCast(Integer page) {
        // Long memberId = 토큰으로 정보 받아오기
        Optional<MainPrefer> userMainCategory = memberPreferRepository.findByMemberId(1L);
        //임시로 1L로 설정
        List<CastDTO.CastHomeDTO> castHomeDTOList;

        if (userMainCategory.isEmpty()) {
            throw new UserHandler(ErrorCode.CAST_NOT_FOUND);
        } else {
            Long userCategoryId = userMainCategory.get().getMainCategory().getId();
            Pageable pageable = PageRequest.of(page - 1, 5);
            List<Cast> castMainCategories = castRepository.findTop5ByMainCategoryIdOrderByHitsDesc(userCategoryId, pageable, 1L).getContent();

            castHomeDTOList = castMainCategories.stream().map(cast ->
                    CastDTO.CastHomeDTO.builder()
                            .id(cast.getId())
                            .title(cast.getTitle())
                            .memberName(cast.getMember().getUsername())
                            .audioLength(cast.getAudioLength())
                            .playlistName(playlistRepository.findUserCategoryName(cast.getId()).getName())
                            .build()
            ).toList();
        }

        return castHomeDTOList;
    }

    @Override
    public List<CastDTO.CastHomeDTO> getCast(String keyword) {

        List<Cast> castList = castRepository.castSearch(keyword);

        return castList.stream().map(cast ->
                CastDTO.CastHomeDTO.builder()
                        .id(cast.getId())
                        .title(cast.getTitle())
                        .memberName(cast.getMember().getUsername())
                        .audioLength(cast.getAudioLength())
                        .playlistName(playlistRepository.findUserCategoryName(cast.getId()).getName())
                        .build()
        ).toList();
    }
}
