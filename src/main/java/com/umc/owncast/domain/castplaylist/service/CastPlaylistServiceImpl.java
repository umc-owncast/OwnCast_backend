package com.umc.owncast.domain.castplaylist.service;

import com.umc.owncast.common.exception.handler.UserHandler;
import com.umc.owncast.common.response.status.ErrorCode;
import com.umc.owncast.domain.castplaylist.entity.CastPlaylist;
import com.umc.owncast.domain.castplaylist.repository.CastPlaylistRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CastPlaylistServiceImpl implements CastPlaylistService{

    private final CastPlaylistRepository castPlaylistRepository;

    @Override
    public CastPlaylist findBySentenceId(Long sentenceId, Long memberId) {
        return castPlaylistRepository.findBySentenceId(sentenceId, memberId)
                .orElseThrow(() -> new UserHandler(ErrorCode.CAST_PLAYLIST_NOT_FOUND));
    }
}
