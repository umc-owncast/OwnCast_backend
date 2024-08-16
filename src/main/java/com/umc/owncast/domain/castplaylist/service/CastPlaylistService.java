package com.umc.owncast.domain.castplaylist.service;

import com.umc.owncast.domain.castplaylist.entity.CastPlaylist;

import java.util.Optional;

public interface CastPlaylistService {
    Optional<CastPlaylist> findBySentenceId(Long sentenceId, Long memberId);
}
