package com.umc.owncast.domain.cast.service;

import com.umc.owncast.common.exception.handler.UserHandler;
import com.umc.owncast.common.response.status.ErrorCode;
import com.umc.owncast.domain.cast.entity.Cast;
import com.umc.owncast.domain.cast.repository.CastRepository;
import com.umc.owncast.domain.castplaylist.entity.CastPlaylist;
import com.umc.owncast.domain.castplaylist.repository.CastPlaylistRepository;
import com.umc.owncast.domain.playlist.entity.Playlist;
import com.umc.owncast.domain.playlist.repository.PlaylistRepository;
import com.umc.owncast.domain.sentence.entity.Sentence;
import com.umc.owncast.domain.sentence.repository.SentenceRepository;
import jdk.jshell.spi.ExecutionControl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class CastPlayServiceImpl {
    private final SentenceRepository sentenceRepository;
    private final CastRepository castRepository;

    public void playCast(Long castId) {

        Optional<Cast> optionalCast = castRepository.findById(castId);
        Cast cast;

        if (optionalCast.isEmpty()) {
            throw new UserHandler(ErrorCode.CAST_NOT_FOUND);
        } else {
            cast = optionalCast.get();
        }

        List<Sentence> sentenceList = sentenceRepository.findAllByCastId(castId);

        // 미정


    }
}
