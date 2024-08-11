package com.umc.owncast.domain.castplaylist.service;

import com.umc.owncast.domain.castplaylist.entity.CastPlaylist;

public interface CastPlaylistService {
    CastPlaylist findBySentenceId(Long sentenceId, Long memberId);
}
