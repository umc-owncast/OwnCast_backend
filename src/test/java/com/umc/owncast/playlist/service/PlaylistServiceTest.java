package com.umc.owncast.playlist.service;

import com.umc.owncast.common.annotation.TrackExecutionTime;
import com.umc.owncast.domain.cast.entity.Cast;
import com.umc.owncast.domain.cast.factory.CastFactory;
import com.umc.owncast.domain.castplaylist.entity.CastPlaylist;
import com.umc.owncast.domain.castplaylist.factory.CastPlaylistFactory;
import com.umc.owncast.domain.member.entity.Member;
import com.umc.owncast.domain.member.factory.MemberFactory;
import com.umc.owncast.domain.member.repository.MemberRepository;
import com.umc.owncast.domain.playlist.entity.Playlist;
import com.umc.owncast.domain.playlist.factory.PlaylistFactory;
import com.umc.owncast.domain.playlist.repository.PlaylistRepository;
import com.umc.owncast.domain.cast.repository.CastRepository;
import com.umc.owncast.domain.castplaylist.repository.CastPlaylistRepository;
import com.umc.owncast.domain.playlist.service.PlaylistService;
import jakarta.transaction.Transactional;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@SpringBootTest
@Transactional
public class PlaylistServiceTest {

    @Autowired
    private PlaylistRepository playlistRepository;

    @Autowired
    private CastRepository castRepository;

    @Autowired
    private CastPlaylistRepository castPlaylistRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PlaylistService playlistService;

    private Member member; // 테스트에서 사용할 멤버를 클래스 필드로 선언

    @BeforeEach
    void setUp() {

        // MemberFactory를 통해 멤버 1명을 생성
        member = MemberFactory.createMultipleMembers(1).get(0);
        memberRepository.save(member);

        // CastFactory를 통해 멤버와 연결된 캐스트 10,000개를 생성
        List<Cast> castList = CastFactory.createMultipleCast(10000, member);
        castRepository.saveAll(castList);

        // PlaylistFactory를 통해 멤버와 연결된 플레이리스트 5개를 생성
        List<Playlist> playlistList = PlaylistFactory.createMultiplePlaylist(5, member, "example");
        playlistRepository.saveAll(playlistList);

        // 각 플레이리스트에 대해 2000개씩 범위를 나눠 CastPlaylist를 생성하고 저장
        int chunkSize = 2000; // 범위를 나눌 크기

        playlistList.forEach(playlist -> {
            for (int start = 0; start < castList.size(); start += chunkSize) {
                // 범위 내의 캐스트를 가져오기
                int end = Math.min(start + chunkSize, castList.size()); // 끝 범위 조정
                List<Cast> subCastList = castList.subList(start, end); // 특정 범위의 서브리스트

                // 범위 내의 캐스트를 사용해 CastPlaylist 생성
                List<CastPlaylist> castPlaylists = subCastList.stream()
                        .map(cast -> CastPlaylistFactory.createCastPlaylist(cast, playlist))
                        .collect(Collectors.toList());

                // 생성된 CastPlaylist를 저장
                castPlaylistRepository.saveAll(castPlaylists);
            }
        });

        /*playlistService.updateImage();*/
    }

    /*@Test
    void addPlaylistTest() {
       playlistService.getAllPlaylists(member).forEach(playlistResultDTO -> {
            System.out.println(playlistResultDTO.getPlaylistId());
        });
    }*/

    @Test
    void search() {
        System.out.println(playlistService.getAllMyPlaylists(member, 0));
    }
}

