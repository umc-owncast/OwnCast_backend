package com.umc.owncast.domain.cast.service;

import com.umc.owncast.common.exception.handler.UserHandler;
import com.umc.owncast.common.response.status.ErrorCode;
import com.umc.owncast.domain.cast.dto.CastDTO;
import com.umc.owncast.domain.cast.entity.Cast;
import com.umc.owncast.domain.cast.repository.CastRepository;
import com.umc.owncast.domain.castcategory.entity.CastMainCategory;
import com.umc.owncast.domain.castcategory.repository.CastMainCategoryRepository;
import com.umc.owncast.domain.castplaylist.entity.CastPlaylist;
import com.umc.owncast.domain.castplaylist.repository.CastPlaylistRepository;
import com.umc.owncast.domain.member.entity.Member;
import com.umc.owncast.domain.memberprefer.Repository.MemberPreferRepository;
import com.umc.owncast.domain.memberprefer.entity.MainPrefer;
import com.umc.owncast.domain.playlist.entity.Playlist;
import com.umc.owncast.domain.playlist.repository.PlaylistRepository;
import jdk.jshell.spi.ExecutionControl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class CastSaveServiceImpl {
    private final PlaylistRepository playlistRepository;
    private final CastPlaylistRepository castPlaylistRepository;
    private final CastRepository castRepository;

    public Long saveCast(CastDTO.CastSaveRequestDTO castSaveRequestDTO) {

        // Long memberId = 토큰으로 정보 받아오기
        //임시로 1L로 설정

        Optional<Playlist> optionalCastPlaylist = playlistRepository.findById(castSaveRequestDTO.getPlaylistId());
        Optional<Cast> optionalCast = castRepository.findById(castSaveRequestDTO.getCastId());

        Playlist castPlaylist;
        Cast cast;


        if (optionalCast.isEmpty() || optionalCastPlaylist.isEmpty()){
            throw new UserHandler(ErrorCode.CAST_NOT_FOUND);
        } else {
            cast = optionalCast.get();
            castPlaylist = optionalCastPlaylist.get();
        }

        if(castPlaylistRepository.existsByMemberIdAndCastId(1L, cast.getId())){ // 해당 멤버 id로 이미 캐스트가 존재하는 경우
            throw new UserHandler(ErrorCode.CAST_ALREADY_EXIST);
        }; // 이미 존재하는 경우

        if (!cast.isPublic()) {
            throw new UserHandler(ErrorCode.CAST_PRIVATE);
        }

        CastPlaylist newCastPlaylist = CastPlaylist.builder()
                .cast(cast)
                .playlist(castPlaylist)
                .build();

        castPlaylistRepository.save(newCastPlaylist);

        return newCastPlaylist.getId();
    }
}
