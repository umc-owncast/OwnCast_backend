package com.umc.owncast.playlist.service;


import com.umc.owncast.common.annotation.TrackExecutionTime;
import com.umc.owncast.domain.cast.repository.CastRepository;
import com.umc.owncast.domain.castplaylist.repository.CastPlaylistRepository;
import com.umc.owncast.domain.member.entity.Member;
import com.umc.owncast.domain.playlist.entity.Playlist;
import com.umc.owncast.domain.playlist.repository.PlaylistRepository;
import com.umc.owncast.domain.playlist.service.PlaylistService;
import com.umc.owncast.domain.playlist.service.PlaylistServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

public class PlaylistServiceTest {

    private final PlaylistRepository playlistRepository = Mockito.mock(PlaylistRepository.class);

    private final CastRepository castRepository = Mockito.mock(CastRepository.class);

    private final CastPlaylistRepository castPlaylistRepository  = Mockito.mock(CastPlaylistRepository.class);

    private PlaylistService playlistService;

    @BeforeEach
    public void setUp() {
        playlistService = new PlaylistServiceImpl(playlistRepository, castRepository, castPlaylistRepository);
    }

    @Test
    @TrackExecutionTime
    void addPlaylistTest(){

        Member member = Mockito.mock(Member.class);

        playlistService.addPlaylist(member, "temp");

        verify(playlistRepository).save(any(Playlist.class)); // save 메서드가 호출되었는지 검증
    }
}
